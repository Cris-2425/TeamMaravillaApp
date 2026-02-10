package com.example.teammaravillaapp.page.selectlist

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.teammaravillaapp.R
import com.example.teammaravillaapp.data.repository.lists.ListsRepository
import com.example.teammaravillaapp.data.repository.recipes.RecipesRepository
import com.example.teammaravillaapp.navigation.NavRoute
import com.example.teammaravillaapp.ui.events.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel de la pantalla “Seleccionar lista”.
 *
 * Responsabilidades:
 * - Obtener el id de receta desde navegación ([SavedStateHandle]).
 * - Observar la receta objetivo (si existe) y el listado de listas.
 * - Gestionar el estado de guardado (evitar doble click / reintentos concurrentes).
 * - Ejecutar la operación de negocio: añadir productIds de la receta a una lista.
 * - Emitir eventos one-shot ([UiEvent]) para feedback (snackbars).
 *
 * Motivo:
 * - Mantener la UI libre de lógica de repositorios y de control de concurrencia.
 *
 * @param listsRepository Repositorio de listas (fuente de listas y operaciones de update).
 * @param recipesRepository Repositorio de recetas para resolver la receta objetivo.
 * @param savedStateHandle Fuente de argumentos de navegación. Debe contener [NavRoute.SelectList.ARG_RECIPE_ID].
 *
 * @throws IllegalStateException No se lanza directamente, pero si falta el argumento se usa -1 y
 * la receta será considerada no encontrada.
 *
 * @see SelectListUiState Estado expuesto a UI.
 * @see ListsRepository
 * @see RecipesRepository
 *
 * Ejemplo de uso:
 * {@code
 * val vm: SelectListViewModel = hiltViewModel()
 * val st by vm.uiState.collectAsStateWithLifecycle()
 * }
 */
@HiltViewModel
class SelectListViewModel @Inject constructor(
    private val listsRepository: ListsRepository,
    recipesRepository: RecipesRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    /**
     * Id de receta recibido por navegación.
     *
     * Restricciones:
     * - Si no existe el argumento, se usa -1 (id inválido).
     */
    private val recipeId: Int =
        savedStateHandle.get<Int>(NavRoute.SelectList.ARG_RECIPE_ID) ?: -1

    private val _events = MutableSharedFlow<UiEvent>(extraBufferCapacity = 1)

    /**
     * Flujo de eventos one-shot (snackbar).
     *
     * Motivo:
     * - No mezclar efectos secundarios con el estado estable de UI.
     */
    val events: SharedFlow<UiEvent> = _events.asSharedFlow()

    private val _isSaving = MutableStateFlow(false)

    /**
     * Estado combinado:
     * - receta (puede ser null si no existe)
     * - listas disponibles
     * - flag isSaving
     *
     * @return [StateFlow] con el [SelectListUiState] listo para pintar.
     */
    val uiState: StateFlow<SelectListUiState> =
        combine(
            recipesRepository.observeRecipe(recipeId),
            listsRepository.lists,
            _isSaving
        ) { recipeWithIngredients, lists, isSaving ->
            if (recipeWithIngredients == null) {
                SelectListUiState(
                    isLoading = false,
                    isRecipeNotFound = true,
                    recipe = null,
                    lists = lists,
                    isSaving = isSaving
                )
            } else {
                SelectListUiState(
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

    /**
     * Maneja el click en una lista: añade los ingredientes de la receta a la lista seleccionada.
     *
     * Reglas:
     * - Si no hay receta cargada, falla con snackbar.
     * - Si ya hay una operación de guardado en curso, ignora el nuevo intento.
     * - La navegación hacia el destino debería ocurrir SOLO si la operación finaliza con éxito
     * (por eso se admite un callback [onSuccessNavigate]).
     *
     * @param listId Id de la lista destino.
     * Restricciones:
     * - No debe ser blank.
     * @param onSuccessNavigate Callback opcional para navegar al detalle de lista tras éxito.
     * Restricciones:
     * - Debe ser rápido y no bloquear (se ejecuta en hilo principal).
     *
     * @throws IllegalArgumentException Validación de negocio: listId blank (si decides validarlo).
     * @throws Exception No se propaga: se captura y se notifica por snackbar.
     *
     * @see ListsRepository.updateProductIds
     *
     * Ejemplo de uso:
     * {@code
     * vm.onListClicked(listId) { navController.navigate(...) }
     * }
     */
    internal fun onListClicked(
        listId: String,
        onSuccessNavigate: (() -> Unit)? = null
    ) {
        val recipe = uiState.value.recipe ?: run {
            _events.tryEmit(UiEvent.ShowSnackbar(R.string.snackbar_action_failed))
            return
        }

        if (listId.isBlank()) {
            _events.tryEmit(UiEvent.ShowSnackbar(R.string.snackbar_action_failed))
            return
        }

        viewModelScope.launch {
            if (_isSaving.value) return@launch
            _isSaving.value = true

            val result = runCatching {
                val currentList = listsRepository.get(listId)
                if (currentList == null) {
                    _events.tryEmit(UiEvent.ShowSnackbar(R.string.snackbar_action_failed))
                    return@launch
                }

                val mergedIds = (currentList.productIds + recipe.productIds).distinct()
                listsRepository.updateProductIds(listId, mergedIds)
            }

            result
                .onSuccess {
                    _events.tryEmit(UiEvent.ShowSnackbar(R.string.ingredients_added_to_the_list))
                    onSuccessNavigate?.invoke()
                }
                .onFailure {
                    _events.tryEmit(UiEvent.ShowSnackbar(R.string.snackbar_action_failed))
                }

            _isSaving.update { false }
        }
    }
}