package com.example.teammaravillaapp.page.stats

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.teammaravillaapp.data.repository.StatsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StatsViewModel @Inject constructor(
    private val repository: StatsRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(StatsUiState())
    val uiState: StateFlow<StatsUiState> = _uiState

    init {
        refresh()
    }

    fun refresh() {
        viewModelScope.launch {
            val snap = repository.loadStats()
            _uiState.value = StatsUiState(
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
    }
}