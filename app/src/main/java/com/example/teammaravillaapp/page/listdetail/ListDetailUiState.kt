package com.example.teammaravillaapp.page.listdetail

import com.example.teammaravillaapp.model.Product
import com.example.teammaravillaapp.model.UserList

data class ListDetailUiState(
    val listId: String? = null,
    val userList: UserList? = null,
    val productsInList: List<Product> = emptyList()
) {
    val isEmptyState: Boolean get() = (userList == null)
    val inListNames: Set<String> get() = productsInList.map { it.name }.toSet()
}