package com.example.teammaravillaapp.page.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.teammaravillaapp.model.UserList
import com.example.teammaravillaapp.repository.ListsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val listsRepository: ListsRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch { listsRepository.seedIfEmpty() }

        viewModelScope.launch {
            listsRepository.lists.collectLatest { lists ->
                applySearchAndEmit(lists, _uiState.value.search)
            }
        }
    }

    fun onSearchChange(newValue: String) {
        _uiState.update { it.copy(search = newValue) }
        // re-filtra con lo que haya ya en memoria
        applySearchAndEmit(_uiState.value.recentLists, newValue)
    }

    private fun applySearchAndEmit(
        lists: List<Pair<String, UserList>>,
        search: String
    ) {
        val q = search.trim()
        val filtered = if (q.isBlank()) {
            lists
        } else {
            lists.filter { (_, list) -> list.name.contains(q, ignoreCase = true) }
        }

        _uiState.update { it.copy(recentLists = filtered) }
    }
}