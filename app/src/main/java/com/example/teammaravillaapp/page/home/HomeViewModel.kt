package com.example.teammaravillaapp.page.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.teammaravillaapp.repository.ListsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val listsRepository: ListsRepository
) : ViewModel() {

    private val search = MutableStateFlow("")

    val uiState: StateFlow<HomeUiState> =
        combine(
            search,
            listsRepository.lists,
            listsRepository.observeProgress()
        ) { q, lists, progressMap ->

            val trimmed = q.trim()
            val filtered = if (trimmed.isBlank()) {
                lists
            } else {
                lists.filter { (_, list) -> list.name.contains(trimmed, ignoreCase = true) }
            }

            val rows = filtered.map { (id, list) ->
                HomeListRow(
                    id = id,
                    list = list,
                    progress = progressMap[id] ?: com.example.teammaravillaapp.repository.ListProgress(0, 0)
                )
            }

            HomeUiState(
                search = q,
                recentLists = rows
            )
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = HomeUiState()
        )

    init {
        viewModelScope.launch { listsRepository.seedIfEmpty() }
    }

    fun onSearchChange(newValue: String) {
        search.value = newValue
    }
}