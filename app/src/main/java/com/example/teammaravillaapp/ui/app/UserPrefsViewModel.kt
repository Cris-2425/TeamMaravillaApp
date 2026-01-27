package com.example.teammaravillaapp.page.prefs

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.teammaravillaapp.model.ListStyle
import com.example.teammaravillaapp.model.ProductCategory
import com.example.teammaravillaapp.repository.UserPrefsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class UserPrefsUiState(
    val listStyle: ListStyle = ListStyle.LISTA,
    val categoryVisibility: Map<ProductCategory, Boolean> =
        ProductCategory.entries.associateWith { true }
)

@HiltViewModel
class UserPrefsViewModel @Inject constructor(
    private val prefs: UserPrefsRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(UserPrefsUiState())
    val uiState: StateFlow<UserPrefsUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            prefs.listStyle.collect { style ->
                _uiState.update { it.copy(listStyle = style) }
            }
        }
        viewModelScope.launch {
            prefs.categoryVisibility.collect { map ->
                _uiState.update { it.copy(categoryVisibility = map) }
            }
        }
    }

    fun setListStyle(style: ListStyle) {
        viewModelScope.launch { prefs.setListStyle(style) }
    }

    fun setHiddenCategories(hidden: Set<ProductCategory>) {
        viewModelScope.launch { prefs.setHiddenCategories(hidden) }
    }

    fun toggleCategory(category: ProductCategory) {
        viewModelScope.launch { prefs.toggleCategory(category) }
    }
}