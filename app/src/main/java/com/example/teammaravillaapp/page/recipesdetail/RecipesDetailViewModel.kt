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

/**
 * ViewModel del detalle de receta.
 *
 * Responsabilidades:
 * - Obtener el recipeId desde navegación ([SavedStateHandle]).
 * - Combinar flujos de repositorios (receta, líneas de ingredientes, catálogo, favoritos).
 * - Exponer un [StateFlow] inmutable [uiState] para renderizado.
 * - Emitir eventos one-shot (snackbar) a través de [events].
 *
 * Motivo:
 * - La UI debe ser “tonta”: solo renderiza el estado y dispara acciones.
 *
 * @param savedStateHandle Fuente de argumentos de navegación (contiene [NavRoute.RecipesDetail.ARG_RECIPE_ID]).
 * Restricciones:
 * - Debe contener el id de receta o se considerará inválido (-1).
 * @param recipesRepository Repositorio de recetas (fuente de receta + líneas).
 * @param productRepository Repositorio de productos (catálogo para enriquecer ingredientes).
 * @param toggleFavorite Caso de uso para alternar favorito.
 * @param favoritesRepository Repositorio de favoritos para observar el set de ids favoritos.
 *
 * @throws IllegalStateException No se lanza directamente, pero si falta el argumento de navegación se puede
 * quedar el id en -1 y la receta resultará “no encontrada”.
 *
 * @see RecipesDetailUiState Estado expuesto a UI.
 * @see ToggleFavoriteUseCase Acción de favorito.
 * @see UiEvent Eventos one-shot.
 *
 * Ejemplo de uso:
 * {@code
 * val vm: RecipesDetailViewModel = hiltViewModel()
 * val st by vm.uiState.collectAsStateWithLifecycle()
 * }
 */
@HiltViewModel
class RecipesDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val recipesRepository: RecipesRepository,
    private val productRepository: ProductRepository,
    private val toggleFavorite: ToggleFavoriteUseCase,
    favoritesRepository: FavoritesRepository
) : ViewModel() {

    /**
     * Id de la receta tomado de navegación.
     *
     * Restricciones:
     * - Si no existe el argumento, se usa -1 (id inválido) y la receta se considerará no encontrada.
     */
    private val recipeId: Int =
        savedStateHandle.get<Int>(NavRoute.RecipesDetail.ARG_RECIPE_ID) ?: -1

    private val _events = MutableSharedFlow<UiEvent>(extraBufferCapacity = 1)

    /**
     * Flujo de eventos one-shot para la UI (snackbar, etc.).
     *
     * Motivo:
     * - Evitar poner efectos secundarios dentro de uiState.
     */
    val events = _events.asSharedFlow()

    /**
     * Estado combinado del detalle de receta.
     *
     * Combina:
     * - receta + “recipeWithIng” (si tu repo lo devuelve así)
     * - líneas de ingredientes
     * - catálogo de productos (Room/API) para enriquecer datos (nombre, imagen, etc.)
     * - favoritos para saber si está marcada
     *
     * @return [StateFlow] con [RecipesDetailUiState] listo para renderizado.
     */
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
            // Por si no hay datos
            runCatching { recipesRepository.seedIfEmpty() }
            // Si falla pero hay cache, la UI seguirá funcionando.
            runCatching { productRepository.refreshProducts() }
        }
    }

    /**
     * Alterna el favorito de la receta actual.
     *
     * - Si no hay receta cargada aún, no hace nada.
     * - Si falla, emite un snackbar genérico.
     *
     * @throws Exception No se propaga: se captura y se notifica por [events].
     *
     * Ejemplo de uso:
     * {@code
     * IconButton(onClick = vm::toggleFavorite) { ... }
     * }
     */
    fun toggleFavorite() {
        val id = uiState.value.recipe?.id ?: return
        viewModelScope.launch {
            runCatching { toggleFavorite.run(id) }
                .onFailure {
                    _events.tryEmit(UiEvent.ShowSnackbar(R.string.snackbar_action_failed))
                }
        }
    }
}