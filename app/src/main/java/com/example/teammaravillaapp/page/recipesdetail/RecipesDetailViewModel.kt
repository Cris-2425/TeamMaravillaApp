package com.example.teammaravillaapp.page.recipesdetail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.teammaravillaapp.R
import com.example.teammaravillaapp.data.repository.favorites.FavoritesRepository
import com.example.teammaravillaapp.data.repository.products.ProductRepository
import com.example.teammaravillaapp.data.repository.recipes.RecipesRepository
import com.example.teammaravillaapp.model.Product
import com.example.teammaravillaapp.navigation.NavRoute
import com.example.teammaravillaapp.page.recipesdetail.usecase.ToggleFavoriteUseCase
import com.example.teammaravillaapp.ui.events.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecipesDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val recipesRepository: RecipesRepository,
    private val productRepository: ProductRepository,
    private val toggleFavorite: ToggleFavoriteUseCase,
    favoritesRepository : FavoritesRepository
) : ViewModel() {

    private val recipeId: Int = savedStateHandle.get<Int>(NavRoute.RecipesDetail.ARG_RECIPE_ID) ?: -1

    private val _events = MutableSharedFlow<UiEvent>(extraBufferCapacity = 1)
    val events = _events.asSharedFlow()

    val uiState: StateFlow<RecipesDetailUiState> =
        combine(
            recipesRepository.observeRecipe(recipeId),
            recipesRepository.observeIngredientLines(recipeId),
            productRepository.observeProducts(),
            favoritesRepository.favoriteIds
        ) { recipeWithIng, ingredientLines, catalog, favIds ->

            if (recipeWithIng == null) {
                RecipesDetailUiState(
                    isLoading = false,
                    isNotFound = true
                )
            } else {
                val byId: Map<String, Product> = catalog.associateBy { it.id }

                val enrichedLines = ingredientLines.map { line ->
                    val enrichedProduct = byId[line.product.id] ?: line.product
                    line.copy(product = enrichedProduct)
                }

                RecipesDetailUiState(
                    isLoading = false,
                    isNotFound = false,
                    recipe = recipeWithIng.recipe,
                    ingredients = enrichedLines.map { it.product },
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
            runCatching { recipesRepository.seedIfEmpty() }
            runCatching { productRepository.refreshProducts() }
        }
    }

    fun toggleFavorite() {
        val id = uiState.value.recipe?.id ?: return
        viewModelScope.launch {
            runCatching { toggleFavorite.run(id) }
                .onFailure { _events.tryEmit(UiEvent.ShowSnackbar(R.string.snackbar_action_failed)) }
        }
    }
}