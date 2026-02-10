package com.example.teammaravillaapp.data.repository.favorites

import com.example.teammaravillaapp.di.ApplicationScope
import com.example.teammaravillaapp.data.local.dao.FavoritesDao
import com.example.teammaravillaapp.data.local.entity.FavoriteRecipeEntity
import com.example.teammaravillaapp.data.remote.datasource.favorites.RemoteFavoritesDataSource
import com.example.teammaravillaapp.data.session.SessionStore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Implementación por defecto de [FavoritesRepository].
 *
 * Repositorio **offline-first** para favoritos, con sincronización
 * asociada al usuario autenticado.
 *
 * ### Estrategia general
 * - El estado local (Room) es la única fuente para la UI.
 * - Las operaciones son inmediatas y no dependen de red.
 * - La sincronización remota es best-effort.
 *
 * ### Comportamiento clave
 * - Merge automático entre favoritos locales y remotos al iniciar sesión.
 * - Persistencia consistente entre dispositivos.
 */
@Singleton
class DefaultFavoritesRepository @Inject constructor(
    private val dao: FavoritesDao,
    private val remote: RemoteFavoritesDataSource,
    private val sessionStore: SessionStore,
    @ApplicationScope private val appScope: CoroutineScope
) : FavoritesRepository {

    /**
     * Flujo reactivo con el conjunto de IDs favoritos.
     *
     * Derivado directamente desde Room.
     */
    override val favoriteIds: Flow<Set<Int>> =
        dao.observeIds()
            .map { it.toSet() }

    init {
        /**
         * Sincroniza favoritos al cambiar de usuario (login).
         *
         * Escucha cambios en el token de sesión y ejecuta un merge
         * entre favoritos locales y remotos.
         */
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
     * ### Flujo
     * 1. Actualiza el estado local inmediatamente.
     * 2. Si hay usuario autenticado:
     *    - Sube el estado completo actual al backend (best-effort).
     *
     * La UI nunca espera a la red.
     */
    override suspend fun toggle(recipeId: Int) {
        val isFav = dao.isFavorite(recipeId)
        if (isFav) {
            dao.remove(recipeId)
        } else {
            dao.add(FavoriteRecipeEntity(recipeId))
        }

        val userId = sessionStore.getTokenOrNull() ?: return

        // Subida best-effort con el estado actual completo
        val idsNow = dao.getAllIdsOnce().toSet()
        runCatching {
            remote.saveFavorites(userId, idsNow)
        }
    }

    /**
     * Sincroniza favoritos al iniciar sesión mediante merge.
     *
     * ### Estrategia
     * - Descarga favoritos remotos del usuario.
     * - Obtiene favoritos locales existentes.
     * - Aplica unión (OR lógico).
     * - Persiste el estado merged tanto local como remotamente.
     *
     * Esto garantiza:
     * - No pérdida de favoritos.
     * - Consistencia entre dispositivos.
     */
    private suspend fun syncOnLoginMerge(userId: String) {
        val remoteIds = remote.getFavorites(userId)
        val localIds = dao.getAllIdsOnce().toSet()

        val merged = remoteIds + localIds

        dao.clearAll()
        dao.addAll(
            merged
                .sorted()
                .map { FavoriteRecipeEntity(it) }
        )

        runCatching {
            remote.saveFavorites(userId, merged)
        }
    }
}