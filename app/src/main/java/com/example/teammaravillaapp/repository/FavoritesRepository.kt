package com.example.teammaravillaapp.repository

import kotlinx.coroutines.flow.Flow

interface FavoritesRepository {
    val favoriteIds: Flow<Set<Int>>
    suspend fun toggle(recipeId: Int)
}