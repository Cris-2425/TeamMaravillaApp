package com.example.teammaravillaapp.page.listdetail

sealed interface ListDetailAction {
    data object RemoveChecked : ListDetailAction
    data object UncheckAll : ListDetailAction
    data object ClearList : ListDetailAction

    data class QueryChanged(val value: String) : ListDetailAction
    data class AddProduct(val productId: String) : ListDetailAction
    data class RemoveProduct(val productId: String) : ListDetailAction
    data class ToggleChecked(val productId: String) : ListDetailAction
    data class IncQuantity(val productId: String) : ListDetailAction
    data class DecQuantity(val productId: String) : ListDetailAction
}