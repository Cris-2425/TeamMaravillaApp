package com.example.teammaravillaapp.page.listdetail

import com.example.teammaravillaapp.data.local.entity.ListItemEntity
import com.example.teammaravillaapp.model.Product
import com.example.teammaravillaapp.model.ProductCategory
import com.example.teammaravillaapp.model.UserList

data class ListDetailUiState(
    val listId: String? = null,
    val userList: UserList? = null,

    val isLoadingCatalog: Boolean = true,
    val catalogError: String? = null,
    val catalog: List<Product> = emptyList(),

    // Productos resueltos (para pintar)
    val productsInList: List<Product> = emptyList(),

    // Meta por item (checked/cantidad)
    val itemMeta: Map<String, ListItemEntity> = emptyMap(),

    val recentCatalog: List<Product> = emptyList(),
    val catalogByCategory: Map<ProductCategory, List<Product>> = emptyMap()
) {
    val isEmptyState: Boolean get() = (userList == null)

    val inListIds: Set<String>
        get() = productsInList.map { it.id }.toSet()

    fun isChecked(productId: String): Boolean =
        itemMeta[productId]?.checked ?: false

    fun quantity(productId: String): Int =
        itemMeta[productId]?.quantity ?: 1
}