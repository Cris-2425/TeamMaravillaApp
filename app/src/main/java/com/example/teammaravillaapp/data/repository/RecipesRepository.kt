package com.example.teammaravillaapp.data.repository

import com.example.teammaravillaapp.model.IngredientLine
import com.example.teammaravillaapp.model.RecipeWithIngredients
import kotlinx.coroutines.flow.Flow

interface RecipesRepository {
    val recipes: Flow<List<RecipeWithIngredients>>
    fun observeRecipe(id: Int): Flow<RecipeWithIngredients?>
    suspend fun seedIfEmpty()
    suspend fun getRecipe(id: Int): RecipeWithIngredients?

    // ingredientes con qty/unit
    fun observeIngredientLines(recipeId: Int): Flow<List<IngredientLine>>
}
