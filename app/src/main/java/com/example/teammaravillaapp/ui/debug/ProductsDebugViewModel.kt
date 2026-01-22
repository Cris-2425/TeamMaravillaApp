package com.example.teammaravillaapp.ui.debug

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.teammaravillaapp.data.seed.CatalogSeeder
import com.example.teammaravillaapp.data.seed.buildSeedItemsFromProductData
import com.example.teammaravillaapp.model.Product
import com.example.teammaravillaapp.model.ProductCategory
import com.example.teammaravillaapp.repository.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

data class ProductsDebugUiState(
    val isLoading: Boolean = true,
    val error: String? = null,
    val products: List<Product> = emptyList()
)

@HiltViewModel
class ProductsDebugViewModel @Inject constructor(
    private val repo: ProductRepository,
    private val catalogSeeder: CatalogSeeder
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProductsDebugUiState())
    val uiState: StateFlow<ProductsDebugUiState> = _uiState.asStateFlow()

    init { refresh() }

    fun refresh() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            runCatching { repo.getProducts() }
                .onSuccess { list -> _uiState.update { it.copy(isLoading = false, products = list) } }
                .onFailure { e ->
                    _uiState.update { it.copy(isLoading = false, error = e.message ?: "Error desconocido") }
                }
        }
    }

    fun addDummyProduct() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }

            val id = UUID.randomUUID().toString().take(8)
            val p = Product(
                id = "debug_$id",
                name = "Producto $id",
                category = ProductCategory.FRUITS,
                imageUrl = null
            )

            runCatching { repo.addProduct(p) }
                .onSuccess { refresh() }
                .onFailure { e ->
                    _uiState.update { it.copy(isLoading = false, error = e.message ?: "Error al añadir") }
                }
        }
    }

    fun deleteFirst() {
        val first = _uiState.value.products.firstOrNull() ?: return
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            runCatching { repo.deleteProduct(first.id) }
                .onSuccess { refresh() }
                .onFailure { e ->
                    _uiState.update { it.copy(isLoading = false, error = e.message ?: "Error al borrar") }
                }
        }
    }

    fun updateFirst() {
        val first = _uiState.value.products.firstOrNull() ?: return
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }

            val updated = first.copy(
                name = first.name + " (upd)",
                category = first.category ?: ProductCategory.OTHER
            )

            runCatching { repo.updateProduct(updated) }
                .onSuccess { refresh() }
                .onFailure { e ->
                    _uiState.update { it.copy(isLoading = false, error = e.message ?: "Error al actualizar") }
                }
        }
    }

    /** sube imágenes + guarda JSON en API */
    fun seed() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }

            runCatching {
                val items = buildSeedItemsFromProductData()
                catalogSeeder.seedAll(items)
            }.onSuccess {
                refresh()
            }.onFailure { e ->
                _uiState.update { it.copy(isLoading = false, error = e.message ?: "Error en seed") }
            }
        }
    }
}