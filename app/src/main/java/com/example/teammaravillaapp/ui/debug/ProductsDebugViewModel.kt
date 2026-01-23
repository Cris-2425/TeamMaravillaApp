package com.example.teammaravillaapp.ui.debug

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.teammaravillaapp.data.seed.CatalogSeeder
import com.example.teammaravillaapp.data.seed.buildSeedItemsFromProductData
import com.example.teammaravillaapp.model.Product
import com.example.teammaravillaapp.model.ProductCategory
import com.example.teammaravillaapp.repository.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
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
    val uiState: StateFlow<ProductsDebugUiState> = _uiState

    private var observeJob: Job? = null

    init {
        startObserving()
    }

    private fun startObserving() {
        observeJob?.cancel()
        observeJob = viewModelScope.launch {
            repo.observeProducts()
                .onStart {
                    _uiState.update { it.copy(isLoading = true, error = null) }
                }
                .catch { e ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            error = e.message ?: "Error desconocido"
                        )
                    }
                }
                .collect { list ->
                    _uiState.update {
                        it.copy(isLoading = false, products = list, error = null)
                    }
                }
        }
    }

    /**
     * Refrescar:
     * - basta con llamar a getProducts() para forzar, si el cache está vacío.
     * - si el cache no está vacío, el refresh best-effort ya lo hace CachedProductRepository al observar.
     *
     * Aquí lo dejamos como "ping" manual, y si falla mostramos error.
     */
    fun refresh() {
        viewModelScope.launch {
            runCatching { repo.getProducts() }
                .onFailure { e ->
                    _uiState.update {
                        it.copy(error = e.message ?: "Error desconocido")
                    }
                }
        }
    }

    fun addDummyProduct() {
        viewModelScope.launch {
            val id = UUID.randomUUID().toString().take(8)
            val p = Product(
                id = "debug_$id",
                name = "Producto $id",
                category = ProductCategory.FRUITS,
                imageUrl = null
            )

            runCatching { repo.addProduct(p) }
                .onFailure { e ->
                    _uiState.update { it.copy(error = e.message ?: "Error al añadir") }
                }
        }
    }

    fun deleteFirst() {
        val first = _uiState.value.products.firstOrNull() ?: return
        viewModelScope.launch {
            runCatching { repo.deleteProduct(first.id) }
                .onFailure { e ->
                    _uiState.update { it.copy(error = e.message ?: "Error al borrar") }
                }
        }
    }

    fun updateFirst() {
        val first = _uiState.value.products.firstOrNull() ?: return
        viewModelScope.launch {
            val updated = first.copy(
                name = first.name + " (upd)",
                category = first.category ?: ProductCategory.OTHER
            )

            runCatching { repo.updateProduct(updated) }
                .onFailure { e ->
                    _uiState.update { it.copy(error = e.message ?: "Error al actualizar") }
                }
        }
    }

    /** sube imágenes + guarda JSON en API */
    fun seed() {
        viewModelScope.launch {
            runCatching {
                val items = buildSeedItemsFromProductData()
                catalogSeeder.seedAll(items)
            }.onFailure { e ->
                _uiState.update { it.copy(error = e.message ?: "Error en seed") }
            }
        }
    }
}