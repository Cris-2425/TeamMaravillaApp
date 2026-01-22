package com.example.teammaravillaapp.page.recipesdetail

import com.example.teammaravillaapp.model.Product
import com.example.teammaravillaapp.model.Recipe

data class RecipesDetailUiState(
    val isLoading: Boolean = true,
    val isNotFound: Boolean = false,
    val error: String? = null,
    val recipe: Recipe? = null,
    val ingredients: List<Product> = emptyList(),
    val isFavorite: Boolean = false
)
