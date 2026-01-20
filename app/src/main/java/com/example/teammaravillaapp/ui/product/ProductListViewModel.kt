package com.example.teammaravillaapp.ui.product

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.teammaravillaapp.repository.ProductRepository
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
                val initialSelected = _uiState.value.selectedId ?: products.firstOrNull()?.id
                _uiState.value = ProductListDetailUiState(
                    isLoading = false,
                    products = products,
                    selectedId = initialSelected
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
