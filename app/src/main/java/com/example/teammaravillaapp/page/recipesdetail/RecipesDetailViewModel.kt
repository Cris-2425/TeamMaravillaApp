package com.example.teammaravillaapp.page.recipesdetail

import androidx.lifecycle.ViewModel
import com.example.teammaravillaapp.model.RecipeData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class RecipesDetailViewModel(
    recipeId: Int
) : ViewModel() {

    private val _uiState = MutableStateFlow(RecipesDetailUiState())
    val uiState: StateFlow<RecipesDetailUiState> = _uiState.asStateFlow()

    init {
        val recipe = RecipeData.getRecipeById(recipeId)
        _uiState.value =
            if (recipe == null) {
                RecipesDetailUiState(
                    isLoading = false,
                    isNotFound = true
                )
            } else {
                RecipesDetailUiState(
                    isLoading = false,
                    recipe = recipe
                )
            }
    }
}