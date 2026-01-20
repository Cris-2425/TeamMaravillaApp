package com.example.teammaravillaapp.page.recipesdetail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.teammaravillaapp.model.RecipeData
import com.example.teammaravillaapp.navigation.NavRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class RecipesDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val recipeId: Int =
        savedStateHandle.get<Int>(NavRoute.RecipesDetail.ARG_RECIPE_ID) ?: -1

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