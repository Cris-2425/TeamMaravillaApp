package com.example.teammaravillaapp.page.recipes

import com.example.teammaravillaapp.model.RecipeWithIngredients

data class RecipesUiState(
    val showMine: Boolean = false,
    val favoriteIds: Set<Int> = emptySet(),
    val visibleRecipes: List<RecipeWithIngredients> = emptyList()
)