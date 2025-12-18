package com.example.teammaravillaapp.page.recipesdetail

import com.example.teammaravillaapp.model.Recipe

data class RecipesDetailUiState(
    val isLoading: Boolean = true,
    val recipe: Recipe? = null,
    val isNotFound: Boolean = false
)