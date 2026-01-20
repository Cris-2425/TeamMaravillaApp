package com.example.teammaravillaapp.page.selectlist

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.teammaravillaapp.model.Product
import com.example.teammaravillaapp.model.RecipeData
import com.example.teammaravillaapp.navigation.NavRoute
import com.example.teammaravillaapp.repository.ListsRepository
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
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val recipeId: Int =
        savedStateHandle.get<Int>(NavRoute.SelectList.ARG_RECIPE_ID) ?: -1

    private val _uiState = MutableStateFlow(SelectListUiState())
    val uiState: StateFlow<SelectListUiState> = _uiState.asStateFlow()


    init {
        listsRepository.seedIfEmpty()
        loadRecipe()
        observeLists()
    }

    private fun loadRecipe() {
        val recipe = RecipeData.getRecipeById(recipeId)
        if (recipe == null) {
            _uiState.value = SelectListUiState(
                isLoading = false,
                isRecipeNotFound = true
            )
            return
        }

        _uiState.value = SelectListUiState(
            isLoading = false,
            recipe = recipe,
            lists = emptyList()
        )
    }

    private fun observeLists() {
        viewModelScope.launch {
            listsRepository.lists.collectLatest { lists ->
                _uiState.update { it.copy(lists = lists) }
            }
        }
    }

    /**
     * Añade los productos de la receta a la lista seleccionada.
     * Evita duplicados por `name`.
     */
    fun addRecipeIngredientsToList(listId: String) {
        val recipe = _uiState.value.recipe ?: return
        val currentList = listsRepository.get(listId) ?: return

        val merged = mergeProductsNoDuplicates(
            currentList.products,
            recipe.products
        )

        listsRepository.updateProducts(listId, merged)
    }

    private fun mergeProductsNoDuplicates(
        base: List<Product>,
        add: List<Product>
    ): List<Product> {
        val existing = base.map { it.name }.toHashSet()
        val result = base.toMutableList()
        for (p in add) {
            if (existing.add(p.name)) result.add(p)
        }
        return result
    }
}