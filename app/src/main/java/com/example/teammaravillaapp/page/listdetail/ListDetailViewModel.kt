package com.example.teammaravillaapp.page.listdetail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.teammaravillaapp.model.Product
import com.example.teammaravillaapp.model.ProductCategory
import com.example.teammaravillaapp.model.ProductData
import com.example.teammaravillaapp.model.UserList
import com.example.teammaravillaapp.navigation.NavRoute
import com.example.teammaravillaapp.repository.ListsRepository
import com.example.teammaravillaapp.repository.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ListDetailViewModel @Inject constructor(
    private val listsRepository: ListsRepository,
    private val productRepository: ProductRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val navListId: String? =
        savedStateHandle.get<String>(NavRoute.ListDetail.ARG_LIST_ID)

    private val _uiState = MutableStateFlow(ListDetailUiState(listId = navListId))
    val uiState: StateFlow<ListDetailUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch { listsRepository.seedIfEmpty() }

        observeListHeader()
        observeCatalogReactive()
        observeItemsFlatMapLatest()
    }

    private fun observeListHeader() {
        viewModelScope.launch {
            listsRepository.lists.collectLatest { lists ->
                val resolved = resolveFrom(lists)

                if (resolved == null) {
                    _uiState.update {
                        it.copy(
                            userList = null,
                            productsInList = emptyList(),
                            itemMeta = emptyMap()
                        )
                    }
                } else {
                    val (id, list) = resolved
                    _uiState.update { it.copy(listId = id, userList = list) }
                    resolveProductsInList()
                }
            }
        }
    }

    private fun observeCatalogReactive() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoadingCatalog = true, catalogError = null) }

            productRepository.observeProducts().collectLatest { catalog ->
                val grouped = catalog.groupBy { it.category ?: ProductCategory.OTHER }
                val recent = catalog.take(18)

                _uiState.update {
                    it.copy(
                        isLoadingCatalog = false,
                        catalog = catalog,
                        recentCatalog = recent,
                        catalogByCategory = grouped
                    )
                }
                resolveProductsInList()
            }
        }

        viewModelScope.launch {
            runCatching { productRepository.getProducts() }
                .onFailure {
                    _uiState.update { st ->
                        if (st.catalog.isEmpty()) {
                            val fallback = ProductData.allProducts
                            st.copy(
                                catalogError = "No se pudo cargar el catálogo. Usando fallback local.",
                                isLoadingCatalog = false,
                                catalog = fallback,
                                recentCatalog = fallback.take(18),
                                catalogByCategory = fallback.groupBy { it.category ?: ProductCategory.OTHER }
                            )
                        } else {
                            st.copy(catalogError = "No se pudo refrescar el catálogo.")
                        }
                    }
                    resolveProductsInList()
                }
        }
    }

    /**
     * ✅ Evita collectors duplicados:
     * Observa items de la lista actual con flatMapLatest.
     */
    private fun observeItemsFlatMapLatest() {
        viewModelScope.launch {
            val listIdFlow = listsRepository.lists
                .map { lists -> resolveFrom(lists)?.first }
                .distinctUntilChanged()

            listIdFlow
                .filterNotNull()
                .flatMapLatest { id -> listsRepository.observeItems(id) }
                .collectLatest { items ->
                    _uiState.update { st ->
                        st.copy(itemMeta = items.associateBy { it.productId })
                    }
                }
        }
    }

    private fun resolveFrom(lists: List<Pair<String, UserList>>): Pair<String, UserList>? =
        when {
            navListId != null -> lists.firstOrNull { it.first == navListId }
            else -> lists.lastOrNull()
        }

    private fun resolveProductsInList() {
        val state = _uiState.value
        val list = state.userList ?: return
        val catalog = state.catalog

        if (catalog.isEmpty()) {
            _uiState.update { it.copy(productsInList = emptyList()) }
            return
        }

        val byId = catalog.associateBy { it.id }
        val resolved: List<Product> = list.productIds.mapNotNull { byId[it] }

        _uiState.update { it.copy(productsInList = resolved) }
    }

    // ---- acciones: item ----

    fun addProduct(product: Product) {
        val listId = _uiState.value.listId ?: return
        viewModelScope.launch {
            listsRepository.addItem(listId, product.id)
        }
    }

    fun removeProduct(product: Product) {
        val listId = _uiState.value.listId ?: return
        viewModelScope.launch {
            listsRepository.removeItem(listId, product.id)
        }
    }

    fun toggleChecked(productId: String) {
        val listId = _uiState.value.listId ?: return
        viewModelScope.launch {
            listsRepository.toggleChecked(listId, productId)
        }
    }

    fun incQuantity(productId: String) {
        val listId = _uiState.value.listId ?: return
        val current = _uiState.value.quantity(productId)
        viewModelScope.launch {
            listsRepository.setQuantity(listId, productId, current + 1)
        }
    }

    fun decQuantity(productId: String) {
        val listId = _uiState.value.listId ?: return
        val current = _uiState.value.quantity(productId)
        val next = (current - 1).coerceAtLeast(1)
        viewModelScope.launch {
            listsRepository.setQuantity(listId, productId, next)
        }
    }

    // ---- acciones: menú contextual (compatibles con tu ListsRepository actual) ----

    /**
     * Elimina solo los productos marcados como comprados.
     * Implementación "segura": recalcula ids y usa updateProductIds (que preserva meta del resto).
     */
    fun removeCheckedItems() {
        val listId = _uiState.value.listId ?: return
        val currentList = _uiState.value.userList ?: return

        val checkedIds: Set<String> =
            _uiState.value.itemMeta.values
                .asSequence()
                .filter { it.checked }
                .map { it.productId }
                .toSet()

        if (checkedIds.isEmpty()) return

        val remaining = currentList.productIds.filterNot { it in checkedIds }

        viewModelScope.launch {
            listsRepository.updateProductIds(listId, remaining)
        }
    }

    /**
     * Desmarca todos los items que estén marcados.
     * No requiere métodos nuevos: usa toggleChecked en los marcados.
     */
    fun uncheckAll() {
        val listId = _uiState.value.listId ?: return

        val checkedIds: List<String> =
            _uiState.value.itemMeta.values
                .asSequence()
                .filter { it.checked }
                .map { it.productId }
                .toList()

        if (checkedIds.isEmpty()) return

        viewModelScope.launch {
            checkedIds.forEach { pid ->
                listsRepository.toggleChecked(listId, pid) // lo deja en false
            }
        }
    }

    /**
     * Vacía la lista completa.
     */
    fun clearList() {
        val listId = _uiState.value.listId ?: return
        viewModelScope.launch {
            listsRepository.updateProductIds(listId, emptyList())
        }
    }
}