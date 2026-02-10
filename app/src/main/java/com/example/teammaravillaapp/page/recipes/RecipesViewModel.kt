package com.example.teammaravillaapp.page.recipes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.teammaravillaapp.data.repository.favorites.FavoritesRepository
import com.example.teammaravillaapp.data.repository.recipes.RecipesRepository
import com.example.teammaravillaapp.model.RecipeWithIngredients
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel de Recetas.
 *
 * Responsabilidades:
 * - Obtener el catálogo de recetas (source of truth) desde [RecipesRepository].
 * - Obtener favoritos desde [FavoritesRepository].
 * - Mantener el estado de filtro (“todas” vs “mías”).
 * - Componer un [RecipesUiState] listo para la UI, evitando lógica en composables.
 *
 * @param recipesRepository Repositorio de recetas. Restricciones: no nulo.
 * @param favoritesRepository Repositorio de favoritos. Restricciones: no nulo.
 *
 * @see RecipesUiState Estado consumido por la UI.
 *
 * Ejemplo de uso:
 * {@code
 * val uiState by vm.uiState.collectAsStateWithLifecycle()
 * vm.setShowMine(true)
 * vm.toggleFavorite(12)
 * }
 */
@HiltViewModel
class RecipesViewModel @Inject constructor(
    private val recipesRepository: RecipesRepository,
    private val favoritesRepository: FavoritesRepository
) : ViewModel() {

    /** Filtro local: true = “solo favoritas”, false = “todas”. */
    private val showMine = MutableStateFlow(false)

    /**
     * Estado observable de la pantalla.
     *
     * @return Un [StateFlow] caliente con el estado consolidado y listo para renderizar.
     */
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
                isLoading = false,
                error = null,
                showMine = showMineNow,
                favoriteIds = favIds,
                visibleRecipes = visible
            )
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = RecipesUiState(isLoading = true)
        )

    init {
        viewModelScope.launch {
            // Si falla, seguimos con lo que haya en local.
            runCatching { recipesRepository.seedIfEmpty() }
        }
    }

    /**
     * Activa/desactiva el filtro “Mis recetas”.
     *
     * @param value true para mostrar solo favoritas; false para mostrar todas.
     */
    fun setShowMine(value: Boolean) {
        showMine.value = value
    }

    /**
     * Alterna el estado de favorito de una receta.
     *
     * @param recipeId Identificador de receta. Restricciones: > 0 recomendado.
     */
    fun toggleFavorite(recipeId: Int) {
        viewModelScope.launch {
            favoritesRepository.toggle(recipeId)
        }
    }
}