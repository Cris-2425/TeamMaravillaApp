package com.example.teammaravillaapp.ui.product

import com.example.teammaravillaapp.model.Product

data class ProductListDetailUiState(
    val isLoading: Boolean = true,
    val products: List<Product> = emptyList(),
    val selectedId: String? = null,
    val error: String? = null
) {
    val selectedProduct: Product? get() = products.firstOrNull { it.id == selectedId }
}
