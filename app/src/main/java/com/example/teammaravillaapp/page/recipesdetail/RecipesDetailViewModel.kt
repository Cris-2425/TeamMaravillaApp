package com.example.teammaravillaapp.page.recipesdetail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.teammaravillaapp.navigation.NavRoute
import com.example.teammaravillaapp.repository.FavoritesRepository
import com.example.teammaravillaapp.repository.ListsRepository
import com.example.teammaravillaapp.repository.RecipesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecipesDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val recipesRepository: RecipesRepository,
    private val listsRepository: ListsRepository,
    private val favoritesRepository: FavoritesRepository
) : ViewModel() {

    private val recipeId: Int =
        savedStateHandle.get<Int>(NavRoute.RecipesDetail.ARG_RECIPE_ID) ?: -1

    val uiState: StateFlow<RecipesDetailUiState> =
        combine(
            recipesRepository.observeRecipe(recipeId),
            favoritesRepository.favoriteIds
        ) { recipeWithIng, favIds ->

            if (recipeWithIng == null) {
                RecipesDetailUiState(
                    isLoading = false,
                    isNotFound = true
                )
            } else {
                RecipesDetailUiState(
                    isLoading = false,
                    isNotFound = false,
                    recipe = recipeWithIng.recipe,
                    ingredients = recipeWithIng.ingredients,
                    isFavorite = recipeWithIng.recipe.id in favIds,
                    error = null
                )
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = RecipesDetailUiState(isLoading = true)
        )

    init {
        viewModelScope.launch {
            recipesRepository.seedIfEmpty()
        }
    }

    fun toggleFavorite() {
        val id = uiState.value.recipe?.id ?: return
        viewModelScope.launch {
            favoritesRepository.toggle(id)
        }
    }

    fun addRecipeIngredientsToList(listId: String) {
        val recipe = uiState.value.recipe ?: return
        viewModelScope.launch {
            val currentList = listsRepository.get(listId) ?: return@launch
            val merged = (currentList.productIds + recipe.productIds).distinct()
            listsRepository.updateProductIds(listId, merged)
        }
    }
}