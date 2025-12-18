package com.example.teammaravillaapp.page.listdetail

import androidx.lifecycle.ViewModel
import com.example.teammaravillaapp.data.FakeUserLists
import com.example.teammaravillaapp.model.Product
import com.example.teammaravillaapp.model.UserList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class ListDetailViewModel(
    private val listId: String?
) : ViewModel() {

    private val _uiState = MutableStateFlow(ListDetailUiState())
    val uiState: StateFlow<ListDetailUiState> = _uiState.asStateFlow()

    init {
        load()
    }

    private fun resolve(): Pair<String, UserList>? {
        return when {
            listId != null -> FakeUserLists.get(listId)?.let { listId to it }
            else -> FakeUserLists.all().lastOrNull()
        }
    }

    fun load() {
        val resolved = resolve()
        if (resolved == null) {
            _uiState.value = ListDetailUiState(
                listId = listId,
                userList = null,
                productsInList = emptyList()
            )
        } else {
            val (id, list) = resolved
            _uiState.value = ListDetailUiState(
                listId = id,
                userList = list,
                productsInList = list.products
            )
        }
    }

    fun removeProduct(product: Product) {
        val current = _uiState.value
        val id = current.listId ?: return

        val newProducts = current.productsInList.filterNot { it.name == product.name }
        persistProducts(id, newProducts)
    }

    fun addProduct(product: Product) {
        val current = _uiState.value
        val id = current.listId ?: return

        if (current.productsInList.any { it.name == product.name }) return

        val newProducts = current.productsInList + product
        persistProducts(id, newProducts)
    }

    private fun persistProducts(id: String, newProducts: List<Product>) {
        FakeUserLists.updateProducts(id, newProducts)

        // Actualizamos estado manteniendo el UserList “refrescado”
        val updatedList = FakeUserLists.get(id)

        _uiState.update {
            it.copy(
                userList = updatedList,
                productsInList = newProducts
            )
        }
    }
}