package com.example.teammaravillaapp.page.categoryfilter

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.teammaravillaapp.R
import com.example.teammaravillaapp.data.local.prefs.user.CategoryFilterPrefs
import com.example.teammaravillaapp.model.ProductCategory
import com.example.teammaravillaapp.ui.events.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel de la pantalla **CategoryFilter**.
 *
 * Responsabilidad principal:
 * - Cargar desde DataStore las categorías seleccionadas previamente.
 * - Gestionar el estado de UI (selección actual) de forma reactiva con StateFlow.
 * - Persistir los cambios (guardar o limpiar) en {@link CategoryFilterPrefs}.
 *
 * Motivo:
 * Separar la lógica de negocio de la UI para cumplir MVVM y facilitar pruebas.
 *
 * @param prefs Repositorio de preferencias (DataStore) que persiste el conjunto de categorías seleccionadas.
 *             No debe ser nulo.
 *
 * @see CategoryFilterUiState
 * @see CategoryFilterPrefs
 */
@HiltViewModel
class CategoryFilterViewModel @Inject constructor(
    private val prefs: CategoryFilterPrefs
) : ViewModel() {

    private val _uiState = MutableStateFlow(CategoryFilterUiState())
    /** Estado observable de la pantalla. Expuesto como inmutable. */
    val uiState: StateFlow<CategoryFilterUiState> = _uiState.asStateFlow()

    private val _events = MutableSharedFlow<UiEvent>(extraBufferCapacity = 1)
    /** Eventos “one-shot” para UI (snackbars, navegación, etc.). */
    val events: SharedFlow<UiEvent> = _events.asSharedFlow()

    init {
        // Carga reactiva del estado guardado en DataStore.
        viewModelScope.launch {
            prefs.observeSelected().collect { saved ->
                _uiState.update { it.copy(isLoading = false, selected = saved) }
            }
        }
    }

    /**
     * Alterna (selecciona/deselecciona) una categoría concreta.
     *
     * @param category Categoría a alternar. No nula.
     * @return Unit.
     *
     * @throws IllegalStateException No se lanza explícitamente, pero podría ocurrir si el StateFlow
     *                              estuviera en un estado inválido por una corrupción externa (raro).
     *
     * Ejemplo de uso:
     * {@code
     * vm.onToggle(ProductCategory.FRUITS)
     * }
     */
    fun onToggle(category: ProductCategory) {
        _uiState.update { st ->
            val next =
                if (category in st.selected) st.selected - category
                else st.selected + category
            st.copy(selected = next)
        }
    }

    /**
     * Alterna “Seleccionar todo / Limpiar todo”.
     *
     * Si actualmente están todas las categorías seleccionadas, deja el conjunto vacío.
     * Si no lo están, selecciona todas las categorías disponibles.
     *
     * @return Unit.
     *
     * Ejemplo de uso:
     * {@code
     * vm.onToggleAll()
     * }
     */
    fun onToggleAll() {
        _uiState.update { st ->
            st.copy(selected = if (st.allSelected) emptySet() else st.all)
        }
    }

    /**
     * Persiste la selección actual en DataStore y ejecuta un callback al finalizar.
     *
     * Motivo:
     * - Persistimos en {@link CategoryFilterPrefs} para que el filtro se aplique en otras pantallas
     *   (ej. ListDetail) de forma consistente.
     *
     * @param onSaved Callback que se ejecuta cuando el guardado termina correctamente (por ejemplo,
     *                navegar hacia atrás). No debe ser nulo.
     * @return Unit.
     *
     * @throws Exception Si falla el acceso a DataStore (IO, corrupción, etc.). Se captura internamente
     *                   y se emite un {@link UiEvent.ShowSnackbar} con error genérico.
     *
     * Ejemplo de uso:
     * {@code
     * vm.onSave { navController.popBackStack() }
     * }
     */
    fun onSave(onSaved: () -> Unit) {
        val selected = _uiState.value.selected
        viewModelScope.launch {
            try {
                prefs.saveSelected(selected)
                onSaved()
            } catch (e: Exception) {
                _events.tryEmit(UiEvent.ShowSnackbar(R.string.snackbar_action_failed))
            }
        }
    }

    /**
     * Limpia la selección guardada en DataStore (equivalente a “sin filtro”).
     *
     * @return Unit.
     *
     * @throws Exception Si falla el acceso a DataStore. Se captura internamente y se notifica con Snackbar.
     *
     * Ejemplo de uso:
     * {@code
     * vm.onClearAll()
     * }
     */
    fun onClearAll() {
        viewModelScope.launch {
            try {
                prefs.clear()
            } catch (e: Exception) {
                _events.tryEmit(UiEvent.ShowSnackbar(R.string.snackbar_action_failed))
            }
        }
    }
}