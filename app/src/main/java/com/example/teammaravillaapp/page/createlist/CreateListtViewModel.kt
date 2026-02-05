package com.example.teammaravillaapp.page.createlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.teammaravillaapp.R
import com.example.teammaravillaapp.data.repository.lists.ListsRepository
import com.example.teammaravillaapp.data.repository.products.ProductRepository
import com.example.teammaravillaapp.data.seed.ProductData
import com.example.teammaravillaapp.model.ListBackground
import com.example.teammaravillaapp.model.UserList
import com.example.teammaravillaapp.ui.events.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreateListViewModel @Inject constructor(
    private val listsRepository: ListsRepository,
    private val productRepository: ProductRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(CreateListUiState(isLoadingCatalog = true))
    val uiState: StateFlow<CreateListUiState> = _uiState.asStateFlow()

    private val _events = MutableSharedFlow<UiEvent>(extraBufferCapacity = 1)
    val events: SharedFlow<UiEvent> = _events.asSharedFlow()

    init {
        refreshCatalog()
    }

    fun refreshCatalog() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoadingCatalog = true, catalogErrorResId = null) }

            val local = runCatching { productRepository.getLocalProducts() }.getOrDefault(emptyList())

            _uiState.update {
                it.copy(
                    isLoadingCatalog = false,
                    catalogProducts = local,
                    catalogErrorResId = null
                )
            }

            // Best-effort refresh (no bloquea UI)
            val refresh = runCatching { productRepository.refreshProducts() }.isSuccess

            if (!refresh && local.isEmpty()) {
                // si no hay nada local, hacemos seed de emergencia
                _events.tryEmit(UiEvent.ShowSnackbar(R.string.snackbar_catalog_seeded))
                _uiState.update {
                    it.copy(
                        catalogErrorResId = R.string.snackbar_catalog_seeded,
                        catalogProducts = ProductData.allProducts
                    )
                }
                runCatching { productRepository.saveProducts(ProductData.allProducts) }
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
        val name = state.trimmedName

        if (name.isBlank()) {
            // la UI decidirá el nombre final con stringResource (más i18n)
            // aquí solo avisamos opcionalmente
            _events.tryEmit(UiEvent.ShowSnackbar(R.string.snackbar_list_name_defaulted))
        }

        val newList = UserList(
            id = "",
            name = name, // lo normal: guardar lo que haya; si está vacío, el repo podría normalizar o lo haces en UI
            background = state.selectedBackground,
            productIds = state.selectedProducts.map { it.id }
        )

        viewModelScope.launch {
            runCatching { listsRepository.add(newList) }
                .onSuccess { id -> onListCreated(id) }
                .onFailure {
                    _events.tryEmit(UiEvent.ShowSnackbar(R.string.snackbar_action_failed))
                }
        }
    }
}