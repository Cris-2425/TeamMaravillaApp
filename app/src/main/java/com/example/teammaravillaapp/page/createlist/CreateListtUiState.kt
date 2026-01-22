package com.example.teammaravillaapp.page.createlist

import com.example.teammaravillaapp.model.ListBackground
import com.example.teammaravillaapp.model.Product

data class CreateListUiState(
    val name: String = "",
    val selectedBackground: ListBackground = ListBackground.FONDO1,
    val catalogLoading: Boolean = false,
    val catalogError: String? = null,
    val catalogProducts: List<Product> = emptyList(),

    // productos que tendrá la lista al crearla (vía sugeridas)
    val selectedProducts: List<Product> = emptyList()
) {
    val finalName: String get() = name.trim().ifBlank { "Nueva lista" }
}
