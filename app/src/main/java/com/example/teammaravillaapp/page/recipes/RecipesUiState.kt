package com.example.teammaravillaapp.page.recipes

import com.example.teammaravillaapp.model.RecipeWithIngredients

data class RecipesUiState(
    val isLoading: Boolean = true,
    val error: Throwable? = null,

    val showMine: Boolean = false,
    val favoriteIds: Set<Int> = emptySet(),
    val visibleRecipes: List<RecipeWithIngredients> = emptyList()
) {
    val isEmpty: Boolean get() = !isLoading && error == null && visibleRecipes.isEmpty()
}