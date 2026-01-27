package com.example.teammaravillaapp.data.repository

import com.example.teammaravillaapp.data.local.dao.FavoritesDao
import com.example.teammaravillaapp.data.local.entity.FavoriteRecipeEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RoomFavoritesRepository @Inject constructor(
    private val dao: FavoritesDao
) : FavoritesRepository {

    override val favoriteIds: Flow<Set<Int>> =
        dao.observeIds()
            .map { it.toSet() }

    override suspend fun toggle(recipeId: Int) {
        val isFav = dao.isFavorite(recipeId)
        if (isFav) dao.remove(recipeId)
        else dao.add(FavoriteRecipeEntity(recipeId))
    }
}