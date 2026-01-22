package com.example.teammaravillaapp.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface FavoritesRepository {
    val favoriteIds: Flow<Set<Int>>
    fun toggle(recipeId: Int)
}
