package com.example.teammaravillaapp.page.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.teammaravillaapp.data.FakeUserLists
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        loadLists()
    }

    private fun loadLists() {
        viewModelScope.launch {
            FakeUserLists.seedIfEmpty()
            _uiState.update {
                it.copy(recentLists = FakeUserLists.all())
            }
        }
    }

    fun onSearchChange(newValue: String) {
        _uiState.update { it.copy(search = newValue) }
    }

    // 🔜 En el futuro:
    // fun refresh()
    // fun onListDeleted(id: String)
}