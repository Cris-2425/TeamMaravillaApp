package com.example.teammaravillaapp.page.stats

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.teammaravillaapp.R
import com.example.teammaravillaapp.data.local.repository.stats.StatsRepository
import com.example.teammaravillaapp.ui.events.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel de estadísticas.
 *
 * Responsabilidades:
 * - Cargar un snapshot agregado de estadísticas mediante [StatsRepository].
 * - Exponer el estado como [StateFlow] para la UI.
 * - Emitir eventos one-shot (snackbar) ante errores recuperables.
 *
 * @param repository Fuente de datos de estadísticas (DB/local).
 * Restricciones:
 * - No nulo.
 *
 * @see StatsRepository
 * @see StatsUiState
 *
 * Ejemplo de uso:
 * {@code
 * val vm: StatsViewModel = hiltViewModel()
 * val state by vm.uiState.collectAsStateWithLifecycle()
 * }
 */
@HiltViewModel
class StatsViewModel @Inject constructor(
    private val repository: StatsRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(StatsUiState(isLoading = false))
    val uiState: StateFlow<StatsUiState> = _uiState.asStateFlow()

    private val _events = MutableSharedFlow<UiEvent>(extraBufferCapacity = 1)
    val events: SharedFlow<UiEvent> = _events.asSharedFlow()

    init {
        refresh()
    }

    /**
     * Refresca las estadísticas.
     *
     * Motivo: permitir recarga manual (reintento tras error) y carga inicial.
     *
     * Comportamiento:
     * - Mantiene los datos anteriores para evitar “pantalla vacía”.
     * - Marca loading=true.
     * - Si falla, conserva los últimos datos y expone el error + snackbar.
     *
     * @throws Exception No se propaga. Los errores del repositorio se capturan y se reflejan en [uiState].
     *
     * Ejemplo de uso:
     * {@code
     * OutlinedButton(onClick = vm::refresh) { Text("Reintentar") }
     * }
     */

    private var refreshJob: Job? = null

    fun refresh() {
        viewModelScope.launch {
            if (refreshJob?.isActive == true) return@launch
            refreshJob = viewModelScope.launch {
                _uiState.value = _uiState.value.copy(isLoading = true, error = null)

                runCatching { repository.loadStats() }
                    .onSuccess { snap ->
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            error = null,
                            lists = snap.lists,
                            products = snap.products,
                            recipes = snap.recipes,
                            favorites = snap.favorites,
                            totalItems = snap.totalItems,
                            checkedItems = snap.checkedItems,
                            listsLast7Days = snap.listsLast7Days,
                            itemsLast7Days = snap.itemsLast7Days,
                            topProducts = snap.topProducts
                        )
                    }
                    .onFailure { t ->
                        _uiState.value = _uiState.value.copy(isLoading = false, error = t)
                        _events.tryEmit(UiEvent.ShowSnackbar(R.string.snackbar_stats_load_failed))
                    }
            }
        }
    }
}