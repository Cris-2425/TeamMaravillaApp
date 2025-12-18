package com.example.teammaravillaapp.page.recipes

import androidx.lifecycle.ViewModel
import com.example.teammaravillaapp.data.FakeUserRecipes
import com.example.teammaravillaapp.model.RecipeData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class RecipesViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(RecipesUiState())
    val uiState: StateFlow<RecipesUiState> = _uiState.asStateFlow()

    init {
        loadAll()
    }

    private fun loadAll() {
        _uiState.update {
            it.copy(
                showMine = false,
                visibleRecipes = RecipeData.recipes
            )
        }
    }

    fun showAll() {
        _uiState.update {
            it.copy(
                showMine = false,
                visibleRecipes = RecipeData.recipes
            )
        }
    }

    fun showMine() {
        _uiState.update {
            it.copy(
                showMine = true,
                visibleRecipes = FakeUserRecipes.all()
            )
        }
    }
}