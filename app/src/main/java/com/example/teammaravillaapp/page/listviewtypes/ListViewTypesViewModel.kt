package com.example.teammaravillaapp.page.listviewtypes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.teammaravillaapp.R
import com.example.teammaravillaapp.data.local.prefs.user.ListViewTypePrefs
import com.example.teammaravillaapp.model.ListViewType
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
 * ViewModel para la pantalla de selección del tipo de vista.
 *
 * Responsabilidades:
 * - Cargar la preferencia actual desde [ListViewTypePrefs].
 * - Permitir al usuario seleccionar un nuevo [ListViewType] en memoria.
 * - Persistir la selección al guardar.
 * - Emitir eventos one-shot ([UiEvent]) ante errores técnicos de persistencia.
 *
 * @param prefs Abstracción de preferencias persistentes (DataStore).
 * Restricciones:
 * - No nulo.
 *
 * @see ListViewTypesUiState Estado expuesto a UI.
 * @see ListViewTypePrefs Persistencia de la preferencia.
 *
 * Ejemplo:
 * {@code
 * val vm: ListViewTypesViewModel = hiltViewModel()
 * val st by vm.uiState.collectAsStateWithLifecycle()
 * }
 */
@HiltViewModel
class ListViewTypesViewModel @Inject constructor(
    private val prefs: ListViewTypePrefs
) : ViewModel() {

    private val _uiState = MutableStateFlow(ListViewTypesUiState())
    val uiState: StateFlow<ListViewTypesUiState> = _uiState.asStateFlow()

    private val _events = MutableSharedFlow<UiEvent>(extraBufferCapacity = 1)
    val events: SharedFlow<UiEvent> = _events.asSharedFlow()

    init {
        // Carga inicial desde DataStore
        viewModelScope.launch {
            prefs.observe().collect { saved ->
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        selected = saved
                    )
                }
            }
        }
    }

    /**
     * Selecciona un tipo de vista en memoria (sin persistir).
     *
     * @param type Tipo de vista elegido por el usuario.
     * Restricciones:
     * - No nulo (enum).
     *
     * Ejemplo:
     * {@code
     * vm.onSelect(ListViewType.COMPACT)
     * }
     */
    fun onSelect(type: ListViewType) {
        _uiState.update { it.copy(selected = type) }
    }

    /**
     * Persiste la preferencia seleccionada en [ListViewTypePrefs].
     *
     * Motivo: hacer persistente la preferencia para reutilizarla en futuras aperturas.
     *
     * @param onSaved Callback invocado cuando la operación finaliza con éxito.
     * Restricciones:
     * - No nulo.
     *
     * @throws Exception No se propaga hacia UI; si ocurre fallo técnico, se emite snackbar genérico.
     *
     * Ejemplo:
     * {@code
     * vm.onSave { navController.popBackStack() }
     * }
     */
    fun onSave(onSaved: () -> Unit) {
        val selected = _uiState.value.selected
        viewModelScope.launch {
            runCatching { prefs.set(selected) }
                .onSuccess { onSaved() }
                .onFailure { _events.tryEmit(UiEvent.ShowSnackbar(R.string.snackbar_action_failed)) }
        }
    }
}