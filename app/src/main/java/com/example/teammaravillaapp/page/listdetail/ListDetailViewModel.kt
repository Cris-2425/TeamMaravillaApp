package com.example.teammaravillaapp.page.listdetail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.teammaravillaapp.R
import com.example.teammaravillaapp.data.local.prefs.listdetail.ListDetailPrefs
import com.example.teammaravillaapp.data.local.prefs.user.RecentListsPrefs
import com.example.teammaravillaapp.navigation.NavRoute.ListDetail.ARG_LIST_ID
import com.example.teammaravillaapp.page.listdetail.usecase.ListDetailHandleActionUseCase
import com.example.teammaravillaapp.page.listdetail.usecase.ListDetailObserveStateUseCase
import com.example.teammaravillaapp.page.listdetail.usecase.RefreshCatalogUseCase
import com.example.teammaravillaapp.ui.events.UiEvent
import com.example.teammaravillaapp.ui.events.UiResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel de la pantalla **ListDetail** (detalle de una lista).
 *
 * Responsabilidades:
 * - Exponer un [StateFlow] inmutable de [ListDetailUiState] para que la UI pinte sin lógica de negocio.
 * - Gestionar acciones del usuario mediante [ListDetailAction]:
 *   - Acciones de UI (ej. búsqueda) actualizan estado local (query).
 *   - Acciones de dominio/persistencia delegan en [ListDetailHandleActionUseCase].
 * - Emitir eventos de un solo uso (snackbars) a través de [events] con [UiEvent].
 * - Lanzar un refresco “best effort” del catálogo al iniciar ([RefreshCatalogUseCase]).
 * - Registrar la lista como reciente mediante [RecentListsPrefs] cuando procede.
 *
 * Restricciones y buenas prácticas (rúbrica):
 * - No mantiene referencias a UI (Context, Views, NavController).
 * - StateFlow expuesto como inmutable.
 * - Eventos one-shot mediante SharedFlow.
 *
 * @property observeState Use case que compone el [ListDetailUiState] combinando repos/prefs/query.
 * @property handleAction Use case que aplica acciones al repositorio de listas.
 * @property refreshCatalog Use case de refresco de catálogo (API) con fallback a caché/seed.
 * @property listDetailPrefs Preferencias de ListDetail (filtro categorías, tipo de vista).
 * @property recentListsPrefs Preferencias para “listas recientes”.
 * @property savedStateHandle Permite recuperar argumentos de navegación (ARG_LIST_ID).
 *
 * Ejemplo de uso en Screen:
 * {@code
 * val uiState by vm.uiState.collectAsState()
 * LaunchedEffect(Unit) { vm.events.collectLatest { event -> ... } }
 * vm.onAction(ListDetailAction.AddProduct(productId))
 * }
 */
@HiltViewModel
class ListDetailViewModel @Inject constructor(
    observeState: ListDetailObserveStateUseCase,
    private val handleAction: ListDetailHandleActionUseCase,
    private val refreshCatalog: RefreshCatalogUseCase,
    private val listDetailPrefs: ListDetailPrefs,
    private val recentListsPrefs: RecentListsPrefs,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    /**
     * Id de lista recibido por navegación.
     *
     * Puede ser null si la pantalla se abre sin argumento y se desea mostrar la “última lista”.
     */
    private val navListId: String? = savedStateHandle.get<String>(ARG_LIST_ID)

    /**
     * Query de búsqueda controlada por ViewModel.
     *
     * Se inyecta en [observeState] para que el estado resultante incluya [ListDetailUiState.searchResults].
     */
    private val _query = MutableStateFlow("")
    val query: StateFlow<String> = _query.asStateFlow() // opcional: útil para debug o UI separada

    /**
     * Eventos de un solo uso para la UI (Snackbars, navegación, etc).
     *
     * Se expone como SharedFlow (inmutable) y se emite con tryEmit para no bloquear.
     */
    private val _events = MutableSharedFlow<UiEvent>(extraBufferCapacity = 1)
    val events = _events.asSharedFlow()

    /**
     * Estado principal de la pantalla, compuesto de forma reactiva (Flow → StateFlow).
     *
     * - started = WhileSubscribed: evita trabajo si la pantalla no está visible.
     * - initialValue: permite pintar la UI de inmediato.
     */
    private val _uiState: StateFlow<ListDetailUiState> =
        observeState.execute(navListId = navListId, queryFlow = _query)
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = ListDetailUiState(listId = navListId, isLoadingCatalog = true)
            )

    /** Estado expuesto a la UI como inmutable. */
    val uiState: StateFlow<ListDetailUiState> = _uiState

    init {
        // 1) Guardar en “recientes” si venimos con un id explícito.
        navListId?.let { id ->
            viewModelScope.launch { recentListsPrefs.push(id) }
        }

        // 2) Refrescar catálogo con estrategia best-effort (no molestar si ya hay caché).
        viewModelScope.launch {
            when (val res = refreshCatalog.executeBestEffort()) {
                is UiResult.Error -> _events.tryEmit(UiEvent.ShowSnackbar(res.messageResId))
                else -> Unit
            }
        }
    }

    /**
     * Limpia el filtro de categorías seleccionado.
     *
     * Normalmente se invoca desde un menú de la pantalla (ej. “limpiar filtro”).
     */
    fun clearCategoryFilter() {
        viewModelScope.launch { listDetailPrefs.clearCategoryFilter() }
    }

    /**
     * Punto de entrada único para gestionar acciones del usuario.
     *
     * Reglas:
     * - [ListDetailAction.QueryChanged] actualiza solo el estado local (no toca repositorio).
     * - El resto de acciones requieren un `listId` real; si no existe, se ignoran.
     * - Tras acciones relevantes, se emiten snackbars informativos.
     *
     * @param action Acción disparada desde la UI.
     */
    fun onAction(action: ListDetailAction) {
        when (action) {
            is ListDetailAction.QueryChanged -> _query.value = action.value

            else -> {
                val listId = uiState.value.listId ?: return

                viewModelScope.launch {
                    runCatching { handleAction.execute(listId, action) }
                        .onSuccess {
                            when (action) {
                                ListDetailAction.RemoveChecked ->
                                    _events.tryEmit(UiEvent.ShowSnackbar(R.string.snackbar_checked_removed))

                                ListDetailAction.ClearList ->
                                    _events.tryEmit(UiEvent.ShowSnackbar(R.string.snackbar_list_cleared))

                                else -> Unit
                            }
                        }
                        .onFailure {
                            _events.tryEmit(UiEvent.ShowSnackbar(R.string.snackbar_action_failed))
                        }
                }
            }
        }
    }
}