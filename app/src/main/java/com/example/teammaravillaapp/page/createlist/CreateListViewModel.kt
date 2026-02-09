package com.example.teammaravillaapp.page.createlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.teammaravillaapp.R
import com.example.teammaravillaapp.data.repository.lists.ListsRepository
import com.example.teammaravillaapp.data.repository.products.ProductRepository
import com.example.teammaravillaapp.data.seed.ProductData
import com.example.teammaravillaapp.model.ListBackground
import com.example.teammaravillaapp.model.UserList
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
 * ViewModel de la pantalla **CreateList**.
 *
 * Responsabilidad:
 * - Mantener el estado de la UI (StateFlow) para nombre, fondo y selección de productos.
 * - Cargar el catálogo de productos (preferentemente desde cache/local).
 * - Ejecutar el guardado de la lista en el repositorio de listas.
 *
 * Decisiones de diseño:
 * - Carga “best effort”: primero intentamos local para pintar rápido, luego refresco remoto sin bloquear UI.
 * - Si remoto falla y no hay local, se hace seed de emergencia con [ProductData].
 *
 * @param listsRepository Repositorio de listas donde se persiste el resultado final.
 * @param productRepository Repositorio de productos para obtener catálogo local y refrescar remoto.
 *
 * @see CreateListUiState
 * @see ListsRepository
 * @see ProductRepository
 */
@HiltViewModel
class CreateListViewModel @Inject constructor(
    private val listsRepository: ListsRepository,
    private val productRepository: ProductRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(CreateListUiState(isLoadingCatalog = true))
    /** Estado observable e inmutable para la UI. */
    val uiState: StateFlow<CreateListUiState> = _uiState.asStateFlow()

    private val _events = MutableSharedFlow<UiEvent>(extraBufferCapacity = 1)
    /** Eventos one-shot (snackbars). */
    val events: SharedFlow<UiEvent> = _events.asSharedFlow()

    init {
        refreshCatalog()
    }

    /**
     * Refresca el catálogo de productos con enfoque “best effort”.
     *
     * Flujo:
     * 1) Carga local (rápido) para mostrar contenido lo antes posible.
     * 2) Lanza refresh remoto en segundo plano.
     * 3) Si remoto falla y no había local, aplica seed de emergencia.
     *
     * @return Unit.
     *
     * @throws Exception No se propaga; se controla internamente usando `runCatching`.
     *
     * Ejemplo de uso:
     * {@code
     * vm.refreshCatalog()
     * }
     */
    fun refreshCatalog() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoadingCatalog = true, catalogErrorResId = null) }

            val local = runCatching { productRepository.getLocalProducts() }.getOrDefault(emptyList())

            _uiState.update {
                it.copy(
                    isLoadingCatalog = false,
                    catalogProducts = local,
                    catalogErrorResId = null
                )
            }

            // Best-effort refresh (no bloquea UI)
            val refreshOk = runCatching { productRepository.refreshProducts() }.isSuccess

            if (!refreshOk && local.isEmpty()) {
                _events.tryEmit(UiEvent.ShowSnackbar(R.string.snackbar_catalog_seeded))
                _uiState.update {
                    it.copy(
                        catalogErrorResId = R.string.snackbar_catalog_seeded,
                        catalogProducts = ProductData.allProducts
                    )
                }
                runCatching { productRepository.saveProducts(ProductData.allProducts) }
            }
        }
    }

    /**
     * Actualiza el nombre tecleado por el usuario.
     *
     * @param newName Nuevo texto del campo. Puede estar vacío. No debe ser nulo.
     * @return Unit.
     *
     * Ejemplo:
     * {@code
     * vm.onNameChange("Compra semanal")
     * }
     */
    fun onNameChange(newName: String) {
        _uiState.update { it.copy(name = newName) }
    }

    /**
     * Cambia el fondo seleccionado.
     *
     * @param bg Fondo elegido por el usuario. No nulo.
     * @return Unit.
     */
    fun onBackgroundSelect(bg: ListBackground) {
        _uiState.update { it.copy(selectedBackground = bg) }
    }

    /**
     * Aplica una lista sugerida: rellena nombre y selecciona productos del catálogo.
     *
     * @param pickedName Nombre sugerido que se colocará en el campo de nombre.
     * @param productIds Lista de IDs a buscar en el catálogo cargado.
     * @return Unit.
     *
     * @throws IllegalStateException No se lanza explícitamente, pero si el catálogo estuviera vacío
     *                              y las IDs no existieran, simplemente no se resolverán productos.
     *
     * Ejemplo:
     * {@code
     * vm.onSuggestedPicked("BBQ sábado", listOf("meat_01","drink_02"))
     * }
     */
    fun onSuggestedPicked(pickedName: String, productIds: List<String>) {
        val catalog = _uiState.value.catalogProducts
        val byId = catalog.associateBy { it.id }
        val resolved = productIds.mapNotNull { byId[it] }

        _uiState.update {
            it.copy(
                name = pickedName,
                selectedProducts = resolved
            )
        }
    }

    /**
     * Guarda la lista con los datos actuales del estado.
     *
     * Validación:
     * - Si el nombre está vacío tras trim, se notifica con snackbar informativa, pero se deja que
     *   el repositorio/flujo de UI decida el nombre final (si quieres, puedes normalizar aquí).
     *
     * @param onListCreated Callback con el ID resultante de la lista persistida.
     *                      No debe ser nulo.
     * @return Unit.
     *
     * @throws Exception No se propaga; en caso de error se emite Snackbar genérico.
     *
     * Ejemplo:
     * {@code
     * vm.save { newId -> navController.navigate("listDetail/$newId") }
     * }
     */
    fun save(onListCreated: (String) -> Unit) {
        val state = _uiState.value
        val name = state.trimmedName

        if (name.isBlank()) {
            _events.tryEmit(UiEvent.ShowSnackbar(R.string.snackbar_list_name_defaulted))
        }

        val newList = UserList(
            id = "",
            name = name,
            background = state.selectedBackground,
            productIds = state.selectedProducts.map { it.id }
        )

        viewModelScope.launch {
            runCatching { listsRepository.add(newList) }
                .onSuccess { id -> onListCreated(id) }
                .onFailure { _events.tryEmit(UiEvent.ShowSnackbar(R.string.snackbar_action_failed)) }
        }
    }
}