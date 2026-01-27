package com.example.teammaravillaapp.page.createlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.teammaravillaapp.model.ListBackground
import com.example.teammaravillaapp.data.seed.ProductData
import com.example.teammaravillaapp.model.UserList
import com.example.teammaravillaapp.data.repository.ListsRepository
import com.example.teammaravillaapp.data.repository.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreateListViewModel @Inject constructor(
    private val listsRepository: ListsRepository,
    private val productRepository: ProductRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(CreateListUiState())
    val uiState: StateFlow<CreateListUiState> = _uiState.asStateFlow()

    init {
        refreshCatalog()
    }

    fun refreshCatalog() {
        viewModelScope.launch {
            _uiState.update { it.copy(catalogLoading = true, catalogError = null) }

            runCatching { productRepository.getProducts() }
                .onSuccess { products ->
                    _uiState.update { it.copy(catalogLoading = false, catalogProducts = products) }
                }
                .onFailure { e ->
                    _uiState.update {
                        it.copy(
                            catalogLoading = false,
                            catalogError = e.message ?: "No se pudo cargar el catálogo",
                            catalogProducts = ProductData.allProducts
                        )
                    }
                }
        }
    }

    fun onNameChange(newName: String) {
        _uiState.update { it.copy(name = newName) }
    }

    fun onBackgroundSelect(bg: ListBackground) {
        _uiState.update { it.copy(selectedBackground = bg) }
    }

    fun onSuggestedPicked(pickedName: String, productIds: List<String>) {
        val catalog = _uiState.value.catalogProducts
        val byId = catalog.associateBy { it.id }
        val resolved = productIds.mapNotNull { byId[it] }

        _uiState.update {
            it.copy(
                name = pickedName,
                selectedProducts = resolved
            )
        }
    }

    fun save(onListCreated: (String) -> Unit) {
        val state = _uiState.value

        val newList = UserList(
            id = "",
            name = state.finalName,
            background = state.selectedBackground,
            productIds = state.selectedProducts.map { it.id }
        )

        viewModelScope.launch {
            val id = listsRepository.add(newList) // ✅ suspend
            onListCreated(id)
        }
    }
}