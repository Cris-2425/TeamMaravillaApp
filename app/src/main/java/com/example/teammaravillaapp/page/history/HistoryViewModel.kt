package com.example.teammaravillaapp.page.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.teammaravillaapp.R
import com.example.teammaravillaapp.data.prefs.RecentListsPrefs
import com.example.teammaravillaapp.data.repository.ListsRepository
import com.example.teammaravillaapp.ui.events.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HistoryViewModel @Inject constructor(
    private val listsRepository: ListsRepository,
    private val recentPrefs: RecentListsPrefs
) : ViewModel() {

    private val _events = MutableSharedFlow<UiEvent>(extraBufferCapacity = 1)
    val events = _events.asSharedFlow()

    private val _uiState = MutableStateFlow(HistoryUiState(isLoading = true))
    val uiState = _uiState.asStateFlow()

    init {
        observe()
    }

    private fun observe() {
        viewModelScope.launch {
            combine(
                recentPrefs.observeIds(),
                listsRepository.lists
            ) { ids, listsPairs ->
                val map = listsPairs.toMap()

                val rows = ids.mapNotNull { id ->
                    map[id]?.let { list ->
                        HistoryRow(
                            id = id,
                            name = list.name
                        )
                    }
                }

                HistoryUiState(
                    isLoading = false,
                    rows = rows
                )
            }
                .distinctUntilChanged()
                .collect { newState ->
                    _uiState.value = newState
                }
        }
    }

    fun onClearAll() {
        viewModelScope.launch {
            runCatching { recentPrefs.clear() }
                .onFailure { _events.tryEmit(UiEvent.ShowSnackbar(R.string.snackbar_action_failed)) }
        }
    }

    fun onRemove(id: String) {
        viewModelScope.launch {
            runCatching { recentPrefs.remove(id) }
                .onFailure { _events.tryEmit(UiEvent.ShowSnackbar(R.string.snackbar_action_failed)) }
        }
    }
}