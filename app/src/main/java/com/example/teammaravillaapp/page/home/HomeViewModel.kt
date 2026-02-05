package com.example.teammaravillaapp.page.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.teammaravillaapp.R
import com.example.teammaravillaapp.data.local.prefs.RecentListsPrefs
import com.example.teammaravillaapp.data.repository.lists.ListProgress
import com.example.teammaravillaapp.data.repository.lists.ListsRepository
import com.example.teammaravillaapp.ui.events.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val listsRepository: ListsRepository,
    private val recentListsPrefs: RecentListsPrefs
) : ViewModel() {

    private val _search = MutableStateFlow("")
    private val _pendingDeletes = MutableStateFlow<Set<String>>(emptySet())

    private val _events = MutableSharedFlow<UiEvent>(extraBufferCapacity = 1)
    val events = _events.asSharedFlow()

    val uiState: StateFlow<HomeUiState> =
        combine(
            _search,
            listsRepository.lists,
            listsRepository.observeProgress(),
            _pendingDeletes
        ) { q, lists, progressMap, pending ->

            val trimmed = q.trim()

            val filtered = lists
                .filter { it.id !in pending }
                .let { base ->
                    if (trimmed.isBlank()) base
                    else base.filter { it.name.contains(trimmed, ignoreCase = true) }
                }

            val rows = filtered.map { list ->
                val id = list.id
                HomeListRow(
                    id = id,
                    list = list,
                    progress = progressMap[id] ?: ListProgress(0, 0)
                )
            }

            HomeUiState(search = q, rows = rows)
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = HomeUiState()
        )

    init {
        viewModelScope.launch { listsRepository.seedIfEmpty() }
    }

    fun onSearchChange(newValue: String) {
        _search.value = newValue
    }

    fun onOpenList(id: String) {
        viewModelScope.launch { recentListsPrefs.push(id) }
    }

    fun requestDelete(id: String) {
        _pendingDeletes.value = _pendingDeletes.value + id
    }

    fun undoDelete(id: String) {
        _pendingDeletes.value = _pendingDeletes.value - id
    }

    fun commitDelete(id: String) {
        viewModelScope.launch {
            runCatching { listsRepository.deleteById(id) }
                .onFailure { _events.tryEmit(UiEvent.ShowSnackbar(R.string.snackbar_action_failed)) }

            _pendingDeletes.value = _pendingDeletes.value - id
            runCatching { recentListsPrefs.remove(id) }
        }
    }

    fun renameList(id: String, newName: String) {
        viewModelScope.launch {
            runCatching { listsRepository.rename(id, newName) }
                .onFailure { _events.tryEmit(UiEvent.ShowSnackbar(R.string.snackbar_action_failed)) }
        }
    }
}