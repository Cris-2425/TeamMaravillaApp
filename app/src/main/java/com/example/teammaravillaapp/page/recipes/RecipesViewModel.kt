package com.example.teammaravillaapp.page.recipes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.teammaravillaapp.model.RecipeWithIngredients
import com.example.teammaravillaapp.repository.FavoritesRepository
import com.example.teammaravillaapp.repository.RecipesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecipesViewModel @Inject constructor(
    private val recipesRepository: RecipesRepository,
    private val favoritesRepository: FavoritesRepository
) : ViewModel() {

    private val showMine = MutableStateFlow(false)

    val uiState: StateFlow<RecipesUiState> =
        combine(
            showMine,
            favoritesRepository.favoriteIds,
            recipesRepository.recipes
        ) { showMineNow, favIds, recipes ->

            val visible: List<RecipeWithIngredients> =
                if (showMineNow) recipes.filter { it.recipe.id in favIds }
                else recipes

            RecipesUiState(
                showMine = showMineNow,
                favoriteIds = favIds,
                visibleRecipes = visible
            )
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = RecipesUiState()
        )

    init {
        viewModelScope.launch {
            recipesRepository.seedIfEmpty()
        }
    }

    fun showAll() {
        showMine.value = false
    }

    fun showMine() {
        showMine.value = true
    }

    fun toggleFavorite(recipeId: Int) {
        viewModelScope.launch {
            favoritesRepository.toggle(recipeId)
        }
    }
}