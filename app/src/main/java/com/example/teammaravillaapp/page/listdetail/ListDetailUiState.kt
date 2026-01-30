package com.example.teammaravillaapp.page.listdetail

import com.example.teammaravillaapp.model.ListViewType
import com.example.teammaravillaapp.model.Product
import com.example.teammaravillaapp.model.ProductCategory
import com.example.teammaravillaapp.model.UserList

data class ListItemUi(
    val product: Product,
    val checked: Boolean,
    val quantity: Int,
    val position: Int
)

data class ListDetailUiState(
    val listId: String? = null,
    val userList: UserList? = null,

    val isLoadingCatalog: Boolean = true,
    val catalogError: String? = null,

    val viewType: ListViewType = ListViewType.BUBBLES,
    val selectedCategories: Set<ProductCategory> = emptySet(),

    val query: String = "",
    val searchResults: List<Product> = emptyList(),

    val items: List<ListItemUi> = emptyList(),

    val recentAvailable: List<Product> = emptyList(),
    val availableByCategory: Map<ProductCategory, List<Product>> = emptyMap()
) {
    val isEmptyState: Boolean get() = userList == null
    val anyChecked: Boolean get() = items.any { it.checked }
    val canClear: Boolean get() = items.isNotEmpty()
}