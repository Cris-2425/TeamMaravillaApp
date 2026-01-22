package com.example.teammaravillaapp.data.repository

import com.example.teammaravillaapp.data.local.dao.FavoritesDao
import com.example.teammaravillaapp.data.local.entity.FavoriteRecipeEntity
import com.example.teammaravillaapp.repository.FavoritesRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RoomFavoritesRepository @Inject constructor(
    private val dao: FavoritesDao
) : FavoritesRepository {

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    override val favoriteIds: Flow<Set<Int>> =
        dao.observeIds()
            .map { it.toSet() }
            .stateIn(
                scope = scope,
                started = kotlinx.coroutines.flow.SharingStarted.WhileSubscribed(5_000),
                initialValue = emptySet()
            )

    override fun toggle(recipeId: Int) {
        scope.launch {
            val isFav = dao.isFavorite(recipeId)
            if (isFav) dao.remove(recipeId)
            else dao.add(FavoriteRecipeEntity(recipeId))
        }
    }
}