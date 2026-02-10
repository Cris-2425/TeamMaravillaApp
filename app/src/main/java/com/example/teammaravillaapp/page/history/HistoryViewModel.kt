package com.example.teammaravillaapp.page.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.teammaravillaapp.R
import com.example.teammaravillaapp.data.local.prefs.user.RecentListsPrefs
import com.example.teammaravillaapp.data.repository.lists.ListsRepository
import com.example.teammaravillaapp.ui.events.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel de la pantalla de historial (listas abiertas recientemente).
 *
 * Responsabilidades:
 * - Combinar ids persistidos en [RecentListsPrefs] con el catálogo de listas de [ListsRepository].
 * - Transformar el resultado en un [HistoryUiState] consumible por la UI.
 * - Exponer eventos one-shot (snackbars) ante fallos.
 *
 * Decisiones de diseño:
 * - Se usa [combine] + `associateBy` para resolver rápido id -> lista.
 * - Se aplica [distinctUntilChanged] para evitar renders redundantes.
 *
 * @param listsRepository Repositorio de listas (fuente de verdad para nombres).
 * Restricciones:
 * - No nulo.
 * @param recentPrefs Preferencias que almacenan el historial de ids recientes.
 * Restricciones:
 * - No nulo.
 *
 * @see HistoryUiState Estado de UI.
 * @see RecentListsPrefs Persistencia de ids recientes.
 * @see ListsRepository Repositorio de listas.
 *
 * Ejemplo de uso:
 * {@code
 * val vm: HistoryViewModel = hiltViewModel()
 * val uiState by vm.uiState.collectAsStateWithLifecycle()
 * }
 */
@HiltViewModel
class HistoryViewModel @Inject constructor(
    private val listsRepository: ListsRepository,
    private val recentPrefs: RecentListsPrefs
) : ViewModel() {

    private val _events = MutableSharedFlow<UiEvent>(extraBufferCapacity = 1)

    /**
     * Flujo de eventos one-shot de UI (snackbar, etc.).
     *
     * Nota: no garantiza entrega si no hay collectors activos.
     */
    val events = _events.asSharedFlow()

    private val _uiState = MutableStateFlow(HistoryUiState(isLoading = true))

    /**
     * Estado observable de la pantalla.
     *
     * Source of truth para la UI.
     */
    val uiState = _uiState.asStateFlow()

    init {
        observe()
    }

    /**
     * Observa cambios del historial y de las listas disponibles para construir [HistoryUiState].
     *
     * Estrategia:
     * 1) Recibe ids recientes desde [RecentListsPrefs.observeIds].
     * 2) Recibe listas disponibles desde [ListsRepository.lists].
     * 3) Construye un mapa id->lista para resolver rápido.
     * 4) Genera filas en el mismo orden de ids (más reciente primero, si así lo guarda prefs).
     *
     * @throws Exception No se lanza hacia arriba (coroutine), pero fallos de lectura pueden
     * propagarse como cancelación o terminar el collector. Si tu prefs/repo lanza, conviene
     * envolver con runCatching + emitir evento.
     */
    private fun observe() {
        viewModelScope.launch {
            combine(
                recentPrefs.observeIds(),
                listsRepository.lists
            ) { ids, lists ->

                val map = lists.associateBy { it.id }

                val rows = ids.mapNotNull { id ->
                    map[id]?.let { list ->
                        HistoryRow(
                            id = id,
                            name = list.name
                        )
                    }
                }

                HistoryUiState(
                    isLoading = false,
                    rows = rows
                )
            }
                .distinctUntilChanged()
                .collect { newState ->
                    _uiState.value = newState
                }
        }
    }

    /**
     * Borra todo el historial de listas recientes.
     *
     * Motivo: permitir al usuario “resetear” la sección de historial.
     *
     * @throws Exception No se lanza hacia arriba; en caso de fallo se emite snackbar genérico.
     *
     * Ejemplo de uso:
     * {@code
     * TextButton(onClick = vm::onClearAll) { Text("Borrar") }
     * }
     */
    fun onClearAll() {
        viewModelScope.launch {
            runCatching { recentPrefs.clear() }
                .onFailure { _events.tryEmit(UiEvent.ShowSnackbar(R.string.snackbar_action_failed)) }
        }
    }

    /**
     * Elimina una entrada concreta del historial.
     *
     * @param id Id de lista a eliminar del historial.
     * Restricciones:
     * - No debería ser vacío.
     *
     * @throws Exception No se lanza hacia arriba; en caso de fallo se emite snackbar genérico.
     *
     * Ejemplo de uso:
     * {@code
     * TextButton(onClick = { vm.onRemove(row.id) }) { Text("Quitar") }
     * }
     */
    fun onRemove(id: String) {
        viewModelScope.launch {
            runCatching { recentPrefs.remove(id) }
                .onFailure { _events.tryEmit(UiEvent.ShowSnackbar(R.string.snackbar_action_failed)) }
        }
    }
}