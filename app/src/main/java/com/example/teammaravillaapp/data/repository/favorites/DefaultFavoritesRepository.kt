package com.example.teammaravillaapp.data.repository.favorites

import com.example.teammaravillaapp.di.ApplicationScope
import com.example.teammaravillaapp.data.local.repository.favorites.RoomFavoritesRepository
import com.example.teammaravillaapp.data.remote.datasource.favorites.RemoteFavoritesDataSource
import com.example.teammaravillaapp.data.session.SessionStore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.CancellationException

/**
 * Implementación por defecto de [FavoritesRepository] con estrategia **offline-first**.
 *
 * La UI observa exclusivamente el estado local (Room) como **source of truth**. Las escrituras se aplican
 * inmediatamente en local y la sincronización remota se ejecuta como *best-effort* para mantener coherencia
 * entre dispositivos.
 *
 * ## Estrategia de sincronización
 * - **Al iniciar sesión**: realiza un *merge* entre favoritos remotos y locales, priorizando la unión
 *   (`remote ∪ local`) para no perder cambios hechos offline.
 * - **En cada toggle**: persiste en local y empuja el conjunto completo al backend si hay sesión activa.
 *
 * ## Concurrencia
 * - La suscripción a [SessionStore.token] se ejecuta en [appScope] (lifecycle de aplicación).
 * - No bloquea la UI: fallos de red se absorben (`runCatching`) y no invalidan el estado local.
 *
 * @property local Implementación local basada en Room.
 * @property remote Fuente remota para favoritos (por usuario).
 * @property sessionStore Proveedor del usuario autenticado (token/userId).
 * @property appScope Scope de aplicación para tareas de sincronización.
 *
 * @see RoomFavoritesRepository
 * @see RemoteFavoritesDataSource
 * @see SessionStore
 */
@Singleton
class DefaultFavoritesRepository @Inject constructor(
    private val local: RoomFavoritesRepository,
    private val remote: RemoteFavoritesDataSource,
    private val sessionStore: SessionStore,
    @ApplicationScope private val appScope: CoroutineScope
) : FavoritesRepository {

    /**
     * Flujo reactivo del conjunto de IDs favoritos.
     *
     * Se alimenta exclusivamente de Room para garantizar comportamiento offline y consistencia.
     */
    override val favoriteIds: Flow<Set<Int>> =
        local.observeIds()

    /**
     * Suscripción de sincronización en login.
     *
     * Se filtra `null` (sin sesión) y se evita re-sincronizar para el mismo usuario con `distinctUntilChanged`.
     */
    init {
        appScope.launch {
            sessionStore.token
                .filterNotNull()
                .distinctUntilChanged()
                .collect { userId ->
                    syncOnLoginMerge(userId)
                }
        }
    }

    /**
     * Alterna el estado de favorito de una receta.
     *
     * ### Semántica offline-first
     * 1) Escribe en local de forma inmediata.
     * 2) Si hay sesión activa, empuja el conjunto completo a remoto (*best-effort*).
     *
     * Esto evita depender de conectividad para ofrecer feedback instantáneo en UI.
     *
     * @param recipeId ID de la receta a alternar.
     *
     * @throws CancellationException Si la coroutine es cancelada (propaga por diseño).
     */
    override suspend fun toggle(recipeId: Int) {
        val isFav = local.isFavorite(recipeId)
        if (isFav) local.remove(recipeId) else local.add(recipeId)

        val userId = sessionStore.getTokenOrNull() ?: return

        val idsNow = local.getAllIdsOnce()
        runCatching { remote.saveFavorites(userId, idsNow) }
    }

    /**
     * Sincroniza favoritos al iniciar sesión mediante estrategia de **merge**.
     *
     * ### Por qué `merge`
     * Evita pérdida de datos cuando:
     * - el usuario marcó favoritos offline
     * - el backend contiene favoritos de otro dispositivo
     *
     * El resultado final es la unión de ambos conjuntos, que se persiste:
     * - en local (Room) como estado de UI
     * - en remoto como consolidación entre dispositivos
     *
     * Los fallos de red se tratan como estado remoto vacío para no bloquear la experiencia.
     *
     * @param userId Identificador estable del usuario (token).
     */
    private suspend fun syncOnLoginMerge(userId: String) {
        val remoteIds = runCatching { remote.getFavorites(userId) }.getOrElse { emptySet() }
        val localIds = local.getAllIdsOnce()
        val merged = remoteIds + localIds

        local.replaceAll(merged)

        runCatching { remote.saveFavorites(userId, merged) }
    }
}