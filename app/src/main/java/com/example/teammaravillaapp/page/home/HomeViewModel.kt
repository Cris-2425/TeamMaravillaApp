package com.example.teammaravillaapp.page.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.teammaravillaapp.R
import com.example.teammaravillaapp.data.local.prefs.user.RecentListsPrefs
import com.example.teammaravillaapp.data.repository.lists.ListProgress
import com.example.teammaravillaapp.data.repository.lists.ListsRepository
import com.example.teammaravillaapp.ui.events.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel de la pantalla Home.
 *
 * Responsabilidades:
 * - Exponer un [HomeUiState] reactivo para que la UI sea “tonta” (solo pinta).
 * - Aplicar la lógica de filtrado por búsqueda.
 * - Integrar datos de listas + progreso (items marcados/total) en un modelo de presentación [HomeListRow].
 * - Gestionar operaciones del usuario: abrir lista (persistir en recientes), borrar con “soft delete” (undo),
 *   renombrar y commit de borrado.
 * - Emitir eventos one-shot mediante [events] (por ejemplo Snackbar ante errores).
 *
 * Decisiones de diseño:
 * - “Borrado diferido” (pending deletes): al hacer swipe delete se oculta la lista del UIState,
 *   pero no se elimina hasta commit. Esto permite implementar Undo de forma sencilla y estable.
 *
 * @param listsRepository Repositorio de listas (source-of-truth).
 * @param recentListsPrefs Persistencia de listas recientes (DataStore).
 *
 * @see HomeUiState
 * @see HomeListRow
 * @see ListsRepository
 * @see RecentListsPrefs
 */
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val listsRepository: ListsRepository,
    private val recentListsPrefs: RecentListsPrefs
) : ViewModel() {

    /** Texto de búsqueda introducido por el usuario. */
    private val _search = MutableStateFlow("")

    /**
     * Conjunto de ids marcados como “pendientes de borrado” (soft delete).
     * Mientras un id esté aquí, no se renderiza en el estado final.
     */
    private val _pendingDeletes = MutableStateFlow<Set<String>>(emptySet())

    /**
     * Eventos de UI de un solo uso (Snackbars, navegación indirecta, etc.).
     *
     * Importante:
     * - Se expone como Flow inmutable.
     * - Se emite con buffer extra para evitar pérdidas por recomposición.
     */
    private val _events = MutableSharedFlow<UiEvent>(extraBufferCapacity = 1)
    val events = _events.asSharedFlow()

    /**
     * Estado observable de la pantalla Home.
     *
     * Combina:
     * - texto de búsqueda
     * - listas del repositorio
     * - mapa de progreso por lista
     * - ids pendientes de borrado (para ocultarlas temporalmente)
     *
     * La UI solo necesita pintar este estado.
     */
    val uiState: StateFlow<HomeUiState> =
        combine(
            _search,
            listsRepository.lists,
            listsRepository.observeProgress(),
            _pendingDeletes
        ) { q, lists, progressMap, pending ->

            val trimmed = q.trim()

            val filtered = lists
                .filter { it.id !in pending }
                .let { base ->
                    if (trimmed.isBlank()) base
                    else base.filter { it.name.contains(trimmed, ignoreCase = true) }
                }

            val rows = filtered.map { list ->
                val id = list.id
                HomeListRow(
                    id = id,
                    list = list,
                    progress = progressMap[id] ?: ListProgress(0, 0)
                )
            }

            HomeUiState(search = q, rows = rows)
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = HomeUiState()
        )

    /**
     * Inicialización: asegura datos base si la app arranca “vacía”.
     *
     * Nota:
     * - El seed vive en el repositorio, no en UI.
     * - Si ya hay datos, no hace nada.
     */
    init {
        viewModelScope.launch { listsRepository.seedIfEmpty() }
    }

    /**
     * Actualiza el texto de búsqueda.
     *
     * @param newValue Nuevo valor introducido por el usuario (puede ser vacío).
     *
     * Ejemplo:
     * {@code
     * SearchField(value = uiState.search, onValueChange = vm::onSearchChange)
     * }
     */
    fun onSearchChange(newValue: String) {
        _search.value = newValue
    }

    /**
     * Registra una lista como “reciente” al abrirla.
     *
     * Importante:
     * - La navegación real se gestiona en UI/NavHost.
     * - Aquí solo persistimos el hecho de que fue abierta.
     *
     * @param id Identificador de la lista abierta. No debe ser vacío.
     *
     * @throws IllegalArgumentException (posible) si el repositorio/prefs valida ids inválidos.
     *
     * Ejemplo:
     * {@code
     * vm.onOpenList(row.id)
     * navController.navigate("listdetail/${row.id}")
     * }
     */
    fun onOpenList(id: String) {
        viewModelScope.launch { recentListsPrefs.push(id) }
    }

    /**
     * Solicita un borrado “diferido” (soft delete) de una lista.
     *
     * Efecto:
     * - La lista desaparece inmediatamente del [uiState] (se añade a [_pendingDeletes]).
     * - Se puede revertir con [undoDelete].
     * - El borrado definitivo se realiza en [commitDelete].
     *
     * @param id Identificador de la lista a ocultar temporalmente.
     */
    fun requestDelete(id: String) {
        _pendingDeletes.value = _pendingDeletes.value + id
    }

    /**
     * Revierte un borrado diferido.
     *
     * Efecto:
     * - El id se elimina de [_pendingDeletes].
     * - La lista vuelve a mostrarse en [uiState].
     *
     * @param id Identificador de la lista a restaurar.
     */
    fun undoDelete(id: String) {
        _pendingDeletes.value = _pendingDeletes.value - id
    }

    /**
     * Confirma el borrado definitivo de una lista previamente marcada como pendiente.
     *
     * Flujo:
     * 1) Elimina del repositorio.
     * 2) Si falla, emite Snackbar de error.
     * 3) Retira el id de pendientes para no “bloquear” el UI.
     * 4) Limpia el id de recientes (best-effort).
     *
     * @param id Identificador de la lista a borrar definitivamente.
     *
     * @throws Exception (capturada internamente) si falla el repositorio. Se notifica vía Snackbar.
     *
     * Ejemplo:
     * {@code
     * vm.requestDelete(id)
     * // si el usuario confirma:
     * vm.commitDelete(id)
     * // si el usuario deshace:
     * vm.undoDelete(id)
     * }
     */
    fun commitDelete(id: String) {
        viewModelScope.launch {
            runCatching { listsRepository.deleteById(id) }
                .onFailure { _events.tryEmit(UiEvent.ShowSnackbar(R.string.snackbar_action_failed)) }

            _pendingDeletes.value = _pendingDeletes.value - id
            runCatching { recentListsPrefs.remove(id) }
        }
    }

    /**
     * Renombra una lista.
     *
     * Nota:
     * - Se asume que la validación de nombre (no vacío, etc.) se hace en UI o en repositorio.
     * - Si falla, se notifica al usuario con Snackbar genérico.
     *
     * @param id Identificador de la lista a renombrar.
     * @param newName Nuevo nombre. Recomendado: ya “trimmed” y no vacío.
     *
     * @throws Exception (capturada internamente) si el repositorio falla. Se notifica vía [events].
     *
     * Ejemplo:
     * {@code
     * vm.renameList(id, "Compra semanal")
     * }
     */
    fun renameList(id: String, newName: String) {
        viewModelScope.launch {
            runCatching { listsRepository.rename(id, newName) }
                .onFailure { _events.tryEmit(UiEvent.ShowSnackbar(R.string.snackbar_action_failed)) }
        }
    }
}