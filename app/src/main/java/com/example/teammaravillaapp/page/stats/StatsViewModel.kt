package com.example.teammaravillaapp.page.stats

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.teammaravillaapp.R
import com.example.teammaravillaapp.data.local.repository.stats.StatsRepository
import com.example.teammaravillaapp.ui.events.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StatsViewModel @Inject constructor(
    private val repository: StatsRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(StatsUiState(isLoading = true))
    val uiState: StateFlow<StatsUiState> = _uiState.asStateFlow()

    private val _events = MutableSharedFlow<UiEvent>(extraBufferCapacity = 1)
    val events: SharedFlow<UiEvent> = _events.asSharedFlow()

    init {
        refresh()
    }

    fun refresh() {
        viewModelScope.launch {
            // Mantiene lo anterior, solo marca loading
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)

            runCatching { repository.loadStats() }
                .onSuccess { snap ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = null,
                        lists = snap.lists,
                        products = snap.products,
                        recipes = snap.recipes,
                        favorites = snap.favorites,
                        totalItems = snap.totalItems,
                        checkedItems = snap.checkedItems,
                        listsLast7Days = snap.listsLast7Days,
                        itemsLast7Days = snap.itemsLast7Days,
                        topProducts = snap.topProducts
                    )
                }
                .onFailure { t ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = t
                    )
                    _events.tryEmit(UiEvent.ShowSnackbar(R.string.snackbar_stats_load_failed))
                }
        }
    }
}