package com.example.teammaravillaapp.page.selectlist

import androidx.lifecycle.ViewModel
import com.example.teammaravillaapp.data.FakeUserLists
import com.example.teammaravillaapp.model.Product
import com.example.teammaravillaapp.model.RecipeData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class SelectListViewModel(
    private val recipeId: Int
) : ViewModel() {

    private val _uiState = MutableStateFlow(SelectListUiState())
    val uiState: StateFlow<SelectListUiState> = _uiState.asStateFlow()

    init {
        load()
    }

    private fun load() {
        FakeUserLists.seedIfEmpty()

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
            lists = FakeUserLists.all()
        )
    }

    /**
     * Añade los productos de la receta a la lista seleccionada.
     * Evita duplicados por `name`.
     */
    fun addRecipeIngredientsToList(listId: String) {
        val recipe = _uiState.value.recipe ?: return
        val currentList = FakeUserLists.get(listId) ?: return

        val merged = mergeProductsNoDuplicates(
            currentList.products,
            recipe.products
        )

        FakeUserLists.updateProducts(listId, merged)

        // refresca listas (por si subtitle = nº productos)
        _uiState.value = _uiState.value.copy(
            lists = FakeUserLists.all()
        )
    }

    private fun mergeProductsNoDuplicates(
        base: List<Product>,
        add: List<Product>
    ): List<Product> {
        val existing = base.map { it.name }.toHashSet()
        val result = base.toMutableList()
        for (p in add) {
            if (existing.add(p.name)) {
                result.add(p)
            }
        }
        return result
    }
}