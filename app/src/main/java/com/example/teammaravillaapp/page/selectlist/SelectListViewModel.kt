package com.example.teammaravillaapp.page.selectlist

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.teammaravillaapp.navigation.NavRoute
import com.example.teammaravillaapp.repository.ListsRepository
import com.example.teammaravillaapp.repository.RecipesRepository
import com.example.teammaravillaapp.ui.theme.TeamMaravillaAppTheme
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SelectListViewModel @Inject constructor(
    private val listsRepository: ListsRepository,
    private val recipesRepository: RecipesRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val recipeId: Int =
        savedStateHandle.get<Int>(NavRoute.SelectList.ARG_RECIPE_ID) ?: -1

    private val _uiState = MutableStateFlow(SelectListUiState())
    val uiState: StateFlow<SelectListUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch { listsRepository.seedIfEmpty() }
        observeRecipe()
        observeLists()
    }

    private fun observeRecipe() {
        viewModelScope.launch {
            recipesRepository.observeRecipe(recipeId).collectLatest { recipeWithIngredients ->
                _uiState.update {
                    if (recipeWithIngredients == null) {
                        it.copy(
                            isLoading = false,
                            isRecipeNotFound = true
                        )
                    } else {
                        it.copy(
                            isLoading = false,
                            recipe = recipeWithIngredients.recipe
                        )
                    }
                }
            }
        }
    }

    private fun observeLists() {
        viewModelScope.launch {
            listsRepository.lists.collectLatest { lists ->
                _uiState.update { it.copy(lists = lists) }
            }
        }
    }

    fun addRecipeIngredientsToList(listId: String) {
        viewModelScope.launch {
            val recipe = _uiState.value.recipe ?: return@launch
            val currentList = listsRepository.get(listId) ?: return@launch

            val mergedIds = (currentList.productIds + recipe.productIds)
                .distinct()

            listsRepository.updateProductIds(listId, mergedIds)
        }
    }
}