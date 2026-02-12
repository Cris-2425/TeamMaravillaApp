package com.example.teammaravillaapp.data.repository.lists

import com.example.teammaravillaapp.di.ApplicationScope
import com.example.teammaravillaapp.data.local.entity.ListItemEntity
import com.example.teammaravillaapp.data.local.repository.lists.RoomListsRepository
import com.example.teammaravillaapp.data.remote.datasource.lists.RemoteListsDataSource
import com.example.teammaravillaapp.model.UserList
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import javax.inject.Inject
import javax.inject.Singleton
import retrofit2.HttpException
import java.io.IOException

/**
 * Implementación por defecto de [ListsRepository] con estrategia **offline-first**.
 *
 * Room actúa como **source of truth**: la UI observa únicamente estado local. Las mutaciones se aplican
 * primero en local y después se sincronizan con el backend en segundo plano (*best-effort*).
 *
 * ## Estrategia de sincronización
 * - **Pull**: `forceRefresh()` descarga el estado remoto y reemplaza el local.
 * - **Push**: `pushAllToRemote()` sube el *snapshot* completo local y sobrescribe el remoto.
 *
 * ## Control de concurrencia
 * - [refreshMutex] evita refrescos simultáneos.
 * - `lastRefreshMs` + [refreshMinIntervalMs] implementan **throttling** para reducir llamadas repetidas
 *   (p. ej. por recomposiciones o múltiples observadores).
 *
 * @property remote DataSource remoto (contrato, sin acoplarse a implementación).
 * @property local Repositorio Room que materializa y mantiene snapshots.
 * @property appScope Scope de aplicación para disparar refresh en background.
 *
 * @see RemoteListsDataSource
 * @see RoomListsRepository
 */
@Singleton
class DefaultListsRepository @Inject constructor(
    private val remote: RemoteListsDataSource,
    private val local: RoomListsRepository,
    @ApplicationScope private val appScope: CoroutineScope
) : ListsRepository {

    /**
     * Mutex para serializar refresh y evitar duplicación de trabajo.
     */
    private val refreshMutex = Mutex()

    /**
     * Marca de tiempo del último refresh exitoso (o del último push).
     *
     * `@Volatile` permite lecturas coherentes entre hilos sin sincronización pesada.
     */
    @Volatile
    private var lastRefreshMs: Long = 0L

    /**
     * Intervalo mínimo entre refresh automáticos para evitar exceso de IO.
     */
    private val refreshMinIntervalMs: Long = 30_000L

    /**
     * Observa listas desde local y dispara un refresh en background al primer collect.
     *
     * @return `Flow` de listas en dominio.
     */
    override fun observeLists(): Flow<List<UserList>> =
        local.observeLists()
            .onStart { appScope.launch { refreshIfStale() } }

    /**
     * Inserta seed local si está vacío y, si procede, refresca desde remoto.
     *
     * La llamada de red es *best-effort* para no bloquear la inicialización offline.
     */
    override suspend fun seedIfEmpty() {
        local.seedIfEmpty()
        runCatching { refreshIfStale() }
    }

    /**
     * Crea una lista en local y sincroniza a remoto en background (*best-effort*).
     *
     * @param list Lista de dominio a crear.
     * @return ID generado localmente.
     */
    override suspend fun add(list: UserList): String {
        val id = local.add(list)
        runCatching { pushAllToRemote() }
        return id
    }

    /**
     * Recupera una lista por ID desde local.
     *
     * @param id ID de la lista.
     * @return Lista de dominio o `null` si no existe.
     */
    override suspend fun get(id: String): UserList? =
        local.get(id)

    /**
     * Sustituye los `productIds` de una lista en local y sincroniza.
     *
     * Actualiza [lastRefreshMs] para evitar un pull inmediato que sobrescriba el cambio reciente.
     *
     * @param id ID de la lista.
     * @param newProductIds IDs finales de productos.
     */
    override suspend fun updateProductIds(id: String, newProductIds: List<String>) {
        local.updateProductIds(id, newProductIds)
        lastRefreshMs = System.currentTimeMillis()
        runCatching { pushAllToRemote() }
    }

    /**
     * Observa items de una lista desde local.
     *
     * @param listId ID de la lista.
     * @return `Flow` con items de la lista.
     */
    override fun observeItems(listId: String): Flow<List<ListItemEntity>> =
        local.observeItems(listId)

    /**
     * Recupera un item puntual desde local.
     *
     * @param listId ID de la lista.
     * @param productId ID del producto.
     * @return Item o `null` si no existe.
     */
    override suspend fun getItem(listId: String, productId: String): ListItemEntity? =
        local.getItem(listId, productId)

    /**
     * Añade un item en local y sincroniza (*best-effort*).
     */
    override suspend fun addItem(listId: String, productId: String) {
        local.addItem(listId, productId)
        runCatching { pushAllToRemote() }
    }

    /**
     * Elimina un item en local y sincroniza (*best-effort*).
     */
    override suspend fun removeItem(listId: String, productId: String) {
        local.removeItem(listId, productId)
        runCatching { pushAllToRemote() }
    }

    /**
     * Alterna `checked` en local y sincroniza (*best-effort*).
     */
    override suspend fun toggleChecked(listId: String, productId: String) {
        local.toggleChecked(listId, productId)
        runCatching { pushAllToRemote() }
    }

    /**
     * Establece `quantity` en local y sincroniza (*best-effort*).
     */
    override suspend fun setQuantity(listId: String, productId: String, quantity: Int) {
        local.setQuantity(listId, productId, quantity)
        runCatching { pushAllToRemote() }
    }

    /**
     * Marca/desmarca todos los items en local y sincroniza (*best-effort*).
     */
    override suspend fun setAllChecked(listId: String, checked: Boolean) {
        local.setAllChecked(listId, checked)
        runCatching { pushAllToRemote() }
    }

    /**
     * Limpia items marcados en local y sincroniza (*best-effort*).
     */
    override suspend fun clearChecked(listId: String) {
        local.clearChecked(listId)
        runCatching { pushAllToRemote() }
    }

    /**
     * Observa progreso desde local.
     *
     * @return `Flow` con progreso por lista.
     */
    override fun observeProgress(): Flow<Map<String, ListProgress>> =
        local.observeProgress()

    /**
     * Elimina una lista en local y sincroniza (*best-effort*).
     */
    override suspend fun deleteById(id: String) {
        local.deleteById(id)
        lastRefreshMs = System.currentTimeMillis()
        runCatching { pushAllToRemote() }
    }

    /**
     * Renombra una lista en local y sincroniza (*best-effort*).
     */
    override suspend fun rename(id: String, newName: String) {
        local.rename(id, newName)
        lastRefreshMs = System.currentTimeMillis()
        runCatching { pushAllToRemote() }
    }

    /**
     * Incrementa la cantidad en local y sincroniza (*best-effort*).
     */
    override suspend fun incQuantity(listId: String, productId: String) {
        local.incQuantity(listId, productId)
        runCatching { pushAllToRemote() }
    }

    /**
     * Decrementa la cantidad (mínimo 1) en local y sincroniza (*best-effort*).
     */
    override suspend fun decQuantityMin1(listId: String, productId: String) {
        local.decQuantityMin1(listId, productId)
        runCatching { pushAllToRemote() }
    }

    /**
     * Fuerza un refresh desde remoto, devolviendo un [Result] para que la capa superior decida UI.
     *
     * @return `Result.success(Unit)` si completa; `Result.failure` en caso de error de red/HTTP.
     */
    override suspend fun refreshLists(): Result<Unit> =
        runCatching { forceRefresh() }

    /**
     * Fuerza inserción de seed local (si aplica) sin tocar red.
     *
     * @return `Result` con el resultado de la operación.
     */
    override suspend fun forceSeed(): Result<Unit> =
        runCatching {
            local.seedIfEmpty()
            lastRefreshMs = System.currentTimeMillis()
        }

    /**
     * Ejecuta un refresh si ha pasado [refreshMinIntervalMs] desde el último refresh/push.
     *
     * Implementa doble comprobación (antes y dentro del mutex) para minimizar contención
     * cuando múltiples colecciones disparan `onStart` simultáneamente.
     */
    private suspend fun refreshIfStale() {
        val now = System.currentTimeMillis()
        if (now - lastRefreshMs < refreshMinIntervalMs) return

        refreshMutex.withLock {
            val nowLocked = System.currentTimeMillis()
            if (nowLocked - lastRefreshMs < refreshMinIntervalMs) return
            runCatching { forceRefresh() }
        }
    }

    /**
     * Descarga desde remoto y reemplaza el estado local.
     *
     * Actualiza [lastRefreshMs] para reflejar el último pull exitoso.
     */
    private suspend fun forceRefresh() {
        val remoteLists = remote.fetchAll()
        local.saveAllFromRemote(remoteLists)
        lastRefreshMs = System.currentTimeMillis()
    }

    /**
     * Sube el estado local completo (snapshot) y sobrescribe el remoto.
     *
     * Actualiza [lastRefreshMs] para evitar un pull inmediato que pueda sobrescribir cambios recientes.
     *
     * @throws HttpException Si el backend responde con error.
     * @throws IOException Si falla la comunicación de red.
     */
    private suspend fun pushAllToRemote() {
        val snapshot = local.snapshotWithItems()
        remote.overwriteAll(snapshot)
        lastRefreshMs = System.currentTimeMillis()
    }
}