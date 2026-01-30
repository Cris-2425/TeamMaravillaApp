package com.example.teammaravillaapp.page.selectlist

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.teammaravillaapp.R
import com.example.teammaravillaapp.data.repository.ListsRepository
import com.example.teammaravillaapp.data.repository.RecipesRepository
import com.example.teammaravillaapp.navigation.NavRoute
import com.example.teammaravillaapp.ui.events.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SelectListViewModel @Inject constructor(
    private val listsRepository: ListsRepository,
    recipesRepository: RecipesRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val recipeId: Int =
        savedStateHandle.get<Int>(NavRoute.SelectList.ARG_RECIPE_ID) ?: -1

    private val _events = MutableSharedFlow<UiEvent>(extraBufferCapacity = 1)
    val events: SharedFlow<UiEvent> = _events.asSharedFlow()

    private val _isSaving = MutableStateFlow(false)

    val uiState: StateFlow<SelectListUiState> =
        combine(
            recipesRepository.observeRecipe(recipeId),
            listsRepository.lists,
            _isSaving
        ) { recipeWithIngredients, lists, isSaving ->
            when {
                recipeWithIngredients == null -> SelectListUiState(
                    isLoading = false,
                    isRecipeNotFound = true,
                    recipe = null,
                    lists = lists,
                    isSaving = isSaving
                )

                else -> SelectListUiState(
                    isLoading = false,
                    isRecipeNotFound = false,
                    recipe = recipeWithIngredients.recipe,
                    lists = lists,
                    isSaving = isSaving
                )
            }
        }
            .distinctUntilChanged()
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = SelectListUiState()
            )

    init {
        viewModelScope.launch { listsRepository.seedIfEmpty() }
    }

    fun addRecipeIngredientsToList(listId: String) {
        val recipe = uiState.value.recipe ?: run {
            _events.tryEmit(UiEvent.ShowSnackbar(R.string.snackbar_action_failed))
            return
        }

        viewModelScope.launch {
            if (_isSaving.value) return@launch

            _isSaving.value = true

            runCatching {
                val currentList = listsRepository.get(listId) ?: error("List not found")

                val mergedIds = (currentList.productIds + recipe.productIds).distinct()
                listsRepository.updateProductIds(listId, mergedIds)
            }
                .onSuccess {
                    _events.tryEmit(UiEvent.ShowSnackbar(R.string.ingredients_added_to_the_list))
                }
                .onFailure {
                    _events.tryEmit(UiEvent.ShowSnackbar(R.string.snackbar_action_failed))
                }

            _isSaving.value = false
        }
    }
}