package com.example.teammaravillaapp.data.repository.lists

import com.example.teammaravillaapp.di.ApplicationScope
import com.example.teammaravillaapp.data.local.entity.ListItemEntity
import com.example.teammaravillaapp.data.local.repository.lists.RoomListsRepository
import com.example.teammaravillaapp.data.remote.datasource.lists.RemoteListsDataSourceImpl
import com.example.teammaravillaapp.model.UserList
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Implementación por defecto de [ListsRepository].
 *
 * Repositorio **offline-first** que utiliza Room como source of truth
 * y sincroniza listas de usuario con el backend de forma best-effort.
 *
 * ### Estrategia general
 * - La UI observa exclusivamente datos locales.
 * - Todas las mutaciones ocurren primero en Room.
 * - La sincronización remota se realiza en background y nunca bloquea la UI.
 *
 * ### Características clave
 * - Sincronización bidireccional (pull + push).
 * - Protección de concurrencia mediante `Mutex`.
 * - Throttling de refresh para evitar llamadas excesivas.
 * - Uso exclusivo de interfaces en la capa remota (sin acoplamiento a impl).
 */
@Singleton
class DefaultListsRepository @Inject constructor(
    private val remote: RemoteListsDataSourceImpl,
    private val local: RoomListsRepository,
    @ApplicationScope private val appScope: CoroutineScope
) : ListsRepository {

    /**
     * Mutex que garantiza un único refresh remoto activo.
     */
    private val refreshMutex = Mutex()

    /**
     * Marca temporal del último refresh remoto.
     */
    @Volatile
    private var lastRefreshMs: Long = 0L

    /**
     * Intervalo mínimo entre refreshes remotos.
     */
    private val refreshMinIntervalMs: Long = 30_000L

    /**
     * Flujo reactivo de listas observado por la UI.
     *
     * - Emite inmediatamente desde Room.
     * - Dispara un refresh remoto perezoso al iniciar la observación.
     */
    override fun observeLists(): Flow<List<UserList>> =
        local.observeLists()
            .onStart {
                appScope.launch { refreshIfStale() }
            }

    /**
     * Inicializa la base local con datos seed si está vacía.
     *
     * Posteriormente intenta sincronizar con el backend.
     */
    override suspend fun seedIfEmpty() {
        local.seedIfEmpty()
        runCatching { refreshIfStale() }
    }

    /**
     * Crea una nueva lista localmente y devuelve su ID.
     *
     * La sincronización remota se ejecuta en segundo plano (best-effort).
     */
    override suspend fun add(list: UserList): String {
        val id = local.add(list)
        runCatching { pushAllToRemote() }
        return id
    }

    /**
     * Obtiene una lista puntual sin reactividad.
     */
    override suspend fun get(id: String): UserList? =
        local.get(id)

    /**
     * Reemplaza los productos de una lista preservando estado local.
     *
     * Pensado para merges masivos desde backend o seed.
     */
    override suspend fun updateProductIds(
        id: String,
        newProductIds: List<String>
    ) {
        local.updateProductIds(id, newProductIds)
        lastRefreshMs = System.currentTimeMillis()
        runCatching { pushAllToRemote() }
    }

    /**
     * Observa los ítems de una lista específica.
     */
    override fun observeItems(listId: String): Flow<List<ListItemEntity>> =
        local.observeItems(listId)

    /**
     * Obtiene un ítem puntual de una lista.
     */
    override suspend fun getItem(
        listId: String,
        productId: String
    ): ListItemEntity? =
        local.getItem(listId, productId)

    /**
     * Añade un producto a una lista.
     */
    override suspend fun addItem(listId: String, productId: String) {
        local.addItem(listId, productId)
        runCatching { pushAllToRemote() }
    }

    /**
     * Elimina un producto de una lista.
     */
    override suspend fun removeItem(listId: String, productId: String) {
        local.removeItem(listId, productId)
        runCatching { pushAllToRemote() }
    }

    /**
     * Alterna el estado checked de un ítem.
     */
    override suspend fun toggleChecked(listId: String, productId: String) {
        local.toggleChecked(listId, productId)
        runCatching { pushAllToRemote() }
    }

    /**
     * Establece la cantidad de un ítem.
     */
    override suspend fun setQuantity(
        listId: String,
        productId: String,
        quantity: Int
    ) {
        local.setQuantity(listId, productId, quantity)
        runCatching { pushAllToRemote() }
    }

    /**
     * Marca o desmarca todos los ítems de una lista.
     */
    override suspend fun setAllChecked(listId: String, checked: Boolean) {
        local.setAllChecked(listId, checked)
        runCatching { pushAllToRemote() }
    }

    /**
     * Elimina todos los ítems marcados como checked.
     */
    override suspend fun clearChecked(listId: String) {
        local.clearChecked(listId)
        runCatching { pushAllToRemote() }
    }

    /**
     * Observa el progreso de todas las listas.
     *
     * Usado principalmente en pantallas de Home o Dashboard.
     */
    override fun observeProgress(): Flow<Map<String, ListProgress>> =
        local.observeProgress()

    /**
     * Elimina una lista completa.
     */
    override suspend fun deleteById(id: String) {
        local.deleteById(id)
        lastRefreshMs = System.currentTimeMillis()
        runCatching { pushAllToRemote() }
    }

    /**
     * Renombra una lista existente.
     */
    override suspend fun rename(id: String, newName: String) {
        local.rename(id, newName)
        lastRefreshMs = System.currentTimeMillis()
        runCatching { pushAllToRemote() }
    }

    /**
     * Incrementa la cantidad de un ítem.
     */
    override suspend fun incQuantity(listId: String, productId: String) {
        local.incQuantity(listId, productId)
        runCatching { pushAllToRemote() }
    }

    /**
     * Decrementa la cantidad de un ítem con mínimo 1.
     */
    override suspend fun decQuantityMin1(listId: String, productId: String) {
        local.decQuantityMin1(listId, productId)
        runCatching { pushAllToRemote() }
    }

    /**
     * Fuerza un refresh remoto hacia la base local.
     */
    override suspend fun refreshLists(): Result<Unit> =
        runCatching { forceRefresh() }

    /**
     * Fuerza la carga de datos seed sobrescribiendo el estado local.
     *
     * Útil para debug o QA.
     */
    override suspend fun forceSeed(): Result<Unit> =
        runCatching {
            local.seedIfEmpty()
            lastRefreshMs = System.currentTimeMillis()
        }

    /**
     * Ejecuta un refresh remoto solo si el último se considera obsoleto.
     *
     * Protegido por mutex para evitar condiciones de carrera.
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
     * Descarga el estado completo de listas desde el backend
     * y lo persiste localmente.
     */
    private suspend fun forceRefresh() {
        val remoteLists = remote.fetchAll()       // List<UserListSnapshot>
        local.saveAllFromRemote(remoteLists)
        lastRefreshMs = System.currentTimeMillis()
    }

    /**
     * Empuja el estado completo local al backend.
     *
     * Usado tras cualquier mutación local.
     */
    private suspend fun pushAllToRemote() {
        val snapshot = local.snapshotWithItems()  // List<UserListSnapshot>
        remote.overwriteAll(snapshot)
        lastRefreshMs = System.currentTimeMillis()
    }
}