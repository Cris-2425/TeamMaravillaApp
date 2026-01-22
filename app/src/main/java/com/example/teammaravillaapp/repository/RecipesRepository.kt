package com.example.teammaravillaapp.repository

import com.example.teammaravillaapp.model.RecipeWithIngredients
import kotlinx.coroutines.flow.Flow

interface RecipesRepository {
    val recipes: Flow<List<RecipeWithIngredients>>
    fun observeRecipe(id: Int): Flow<RecipeWithIngredients?>
    suspend fun seedIfEmpty()
    suspend fun getRecipe(id: Int): RecipeWithIngredients?
}