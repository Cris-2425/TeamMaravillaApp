package com.example.teammaravillaapp.page.listdetail

import com.example.teammaravillaapp.model.Product
import com.example.teammaravillaapp.model.ProductCategory
import com.example.teammaravillaapp.model.UserList

data class ListDetailUiState(
    val listId: String? = null,

    // Lista seleccionada (dominio)
    val userList: UserList? = null,

    // Catálogo
    val isLoadingCatalog: Boolean = true,
    val catalogError: String? = null,
    val catalog: List<Product> = emptyList(),

    // Ingredientes/productos de la lista (resueltos por ids)
    val productsInList: List<Product> = emptyList(),

    // “Usados recientemente”
    val recentCatalog: List<Product> = emptyList(),

    // Agrupado por categoría para pintar secciones
    val catalogByCategory: Map<ProductCategory, List<Product>> = emptyMap()
) {
    val isEmptyState: Boolean get() = (userList == null)

    val inListIds: Set<String>
        get() = productsInList.map { it.id }.toSet()
}