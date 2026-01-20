package com.example.teammaravillaapp.page.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.teammaravillaapp.repository.ListsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val listsRepository: ListsRepository
) : ViewModel() {

    private val searchQuery = MutableStateFlow("")

    private val _uiState: StateFlow<HomeUiState> =
        combine(
            searchQuery,
            listsRepository.lists
        ) { search, lists ->

            val filtered = if (search.isBlank()) {
                lists
            } else {
                val q = search.trim().lowercase()
                lists.filter { (_, list) ->
                    list.name.lowercase().contains(q)
                }
            }

            HomeUiState(
                search = search,
                recentLists = filtered
            )
        }.stateIn(
            scope = viewModelScope,
            started = kotlinx.coroutines.flow.SharingStarted.WhileSubscribed(5_000),
            initialValue = HomeUiState()
        )

    val uiState: StateFlow<HomeUiState> = _uiState

    init {
        listsRepository.seedIfEmpty()
    }

    fun onSearchChange(newValue: String) {
        searchQuery.value = newValue
    }
}