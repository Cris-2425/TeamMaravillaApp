package com.example.teammaravillaapp.page.createlist

import androidx.annotation.StringRes
import com.example.teammaravillaapp.model.ListBackground
import com.example.teammaravillaapp.model.Product

data class CreateListUiState(
    val name: String = "",
    val selectedBackground: ListBackground = ListBackground.FONDO1,

    val isLoadingCatalog: Boolean = false,
    @StringRes val catalogErrorResId: Int? = null,

    val catalogProducts: List<Product> = emptyList(),

    // productos que tendrá la lista al crearla (vía sugeridas)
    val selectedProducts: List<Product> = emptyList()
) {
    val trimmedName: String get() = name.trim()
}