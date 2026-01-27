package com.example.teammaravillaapp.ui.catalog

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.teammaravillaapp.data.repository.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductListDetailViewModel @Inject constructor(
    private val repo: ProductRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProductListDetailUiState())
    val uiState: StateFlow<ProductListDetailUiState> = _uiState

    init {
        load()
    }

    fun load() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)

            try {
                val products = repo.getProducts()
                // 3) Mantener selección si sigue existiendo; si no, primera opción
                val prevSelected = _uiState.value.selectedId
                val selectedId = when {
                    prevSelected != null && products.any { it.id == prevSelected } -> prevSelected
                    else -> products.firstOrNull()?.id
                }
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    products = products,
                    selectedId = selectedId,
                    error = null
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.message ?: "Error cargando productos"
                )
            }
        }
    }


    fun select(id: String) {
        _uiState.value = _uiState.value.copy(selectedId = id)
    }
}
