package com.example.teammaravillaapp.page.listdetail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.teammaravillaapp.model.Product
import com.example.teammaravillaapp.model.UserList
import com.example.teammaravillaapp.navigation.NavRoute
import com.example.teammaravillaapp.repository.ListsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ListDetailViewModel @Inject constructor(
    private val listsRepository: ListsRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val listId: String? = savedStateHandle.get<String>(NavRoute.ListDetail.ARG_LIST_ID)

    private val _uiState = MutableStateFlow(ListDetailUiState(listId = listId))
    val uiState: StateFlow<ListDetailUiState> = _uiState.asStateFlow()

    init {
        observeList()
    }

    private fun resolveFrom(lists: List<Pair<String, UserList>>): Pair<String, UserList>? =
        when {
            listId != null -> lists.firstOrNull { it.first == listId }
            else -> lists.lastOrNull()
        }

    private fun observeList() {
        listsRepository.seedIfEmpty()

        viewModelScope.launch {
            listsRepository.lists.collect { lists ->
                val resolved = resolveFrom(lists)

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
        }
    }

    fun removeProduct(product: Product) {
        val current = _uiState.value
        val id = current.listId ?: return
        val newProducts = current.productsInList.filterNot { it.id == product.id }
        listsRepository.updateProducts(id, newProducts)
    }

    fun addProduct(product: Product) {
        val current = _uiState.value
        val id = current.listId ?: return
        if (current.productsInList.any { it.id == product.id }) return
        listsRepository.updateProducts(id, current.productsInList + product)
    }
}