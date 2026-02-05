package com.example.teammaravillaapp.data.repository.favorites

import com.example.teammaravillaapp.core.di.ApplicationScope
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

@Singleton
class DefaultFavoritesRepository @Inject constructor(
    private val dao: FavoritesDao,
    private val remote: RemoteFavoritesDataSource,
    private val sessionStore: SessionStore,
    @ApplicationScope private val appScope: CoroutineScope
) : FavoritesRepository {

    override val favoriteIds: Flow<Set<Int>> =
        dao.observeIds().map { it.toSet() }

    init {
        // Sync al cambiar de usuario (login)
        appScope.launch {
            sessionStore.token
                .filterNotNull()
                .distinctUntilChanged()
                .collect { userId ->
                    syncOnLoginMerge(userId)
                }
        }
    }

    override suspend fun toggle(recipeId: Int) {
        val isFav = dao.isFavorite(recipeId)
        if (isFav) dao.remove(recipeId) else dao.add(FavoriteRecipeEntity(recipeId))

        val userId = sessionStore.getTokenOrNull() ?: return

        // Subida best-effort con el estado actual
        val idsNow = dao.getAllIdsOnce().toSet()
        runCatching { remote.saveFavorites(userId, idsNow) }
    }

    private suspend fun syncOnLoginMerge(userId: String) {
        val remoteIds = remote.getFavorites(userId)
        val localIds = dao.getAllIdsOnce().toSet()

        val merged = (remoteIds + localIds)

        // Persistimos merged en local
        dao.clearAll()
        dao.addAll(merged.sorted().map { FavoriteRecipeEntity(it) })

        // Persistimos merged en remoto (para dejarlo consistente)
        runCatching { remote.saveFavorites(userId, merged) }
    }
}