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
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ListDetailViewModel @Inject constructor(
    private val listsRepository: ListsRepository,
    private val productRepository: ProductRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val listId: String? =
        savedStateHandle.get<String>(NavRoute.ListDetail.ARG_LIST_ID)

    private val _uiState = MutableStateFlow(ListDetailUiState(listId = listId))
    val uiState: StateFlow<ListDetailUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch { listsRepository.seedIfEmpty() }
        observeListAndCatalog()
    }

    private fun observeListAndCatalog() {
        viewModelScope.launch {
            listsRepository.lists.collectLatest { lists ->
                val resolved = resolveFrom(lists)

                if (resolved == null) {
                    _uiState.update {
                        it.copy(
                            userList = null,
                            productsInList = emptyList()
                        )
                    }
                } else {
                    val (id, list) = resolved
                    _uiState.update {
                        it.copy(
                            listId = id,
                            userList = list
                        )
                    }
                    resolveProductsInList()
                }
            }
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoadingCatalog = true, catalogError = null) }

            val catalog: List<Product> = runCatching { productRepository.getProducts() }
                .getOrElse {
                    _uiState.update { st ->
                        st.copy(catalogError = "No se pudo cargar el catálogo. Usando fallback local.")
                    }
                    ProductData.allProducts
                }

            val grouped: Map<ProductCategory, List<Product>> =
                catalog.groupBy { it.category ?: ProductCategory.OTHER }

            // "Recientes": simple (primeros N). Si quieres, lo cambiamos luego por timestamp.
            val recent: List<Product> = catalog.take(18)

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

    private fun resolveFrom(lists: List<Pair<String, UserList>>): Pair<String, UserList>? =
        when {
            listId != null -> lists.firstOrNull { it.first == listId }
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

    fun addProduct(product: Product) {
        val id = _uiState.value.listId ?: return
        val current = _uiState.value.userList ?: return

        if (product.id in current.productIds) return

        val newIds = (current.productIds + product.id).distinct()

        viewModelScope.launch {
            listsRepository.updateProductIds(id, newIds)
        }
    }

    fun removeProduct(product: Product) {
        val id = _uiState.value.listId ?: return
        val current = _uiState.value.userList ?: return

        val newIds = current.productIds.filterNot { it == product.id }

        viewModelScope.launch {
            listsRepository.updateProductIds(id, newIds)
        }
    }
}