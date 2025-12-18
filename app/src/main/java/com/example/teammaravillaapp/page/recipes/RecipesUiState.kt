package com.example.teammaravillaapp.page.recipes

import com.example.teammaravillaapp.model.Recipe

data class RecipesUiState(
    val showMine: Boolean = false,
    val visibleRecipes: List<Recipe> = emptyList()
)