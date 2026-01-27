package com.example.teammaravillaapp.page.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.teammaravillaapp.data.repository.ListsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

data class HistoryUiState(
    val ids: List<String> = emptyList(),
    val rows: List<HistoryRow> = emptyList()
)

data class HistoryRow(
    val id: String,
    val name: String
)

@HiltViewModel
class HistoryViewModel @Inject constructor(
    listsRepository: ListsRepository
) : ViewModel() {

    // IDs del historial llegan desde la pantalla (DataStore), por eso se inyectan vía setter.
    private val recentIds = MutableStateFlow<List<String>>(emptyList())

    val uiState: StateFlow<HistoryUiState> =
        combine(
            recentIds,
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
                ids = ids,
                rows = rows
            )
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = HistoryUiState()
        )

    fun setRecentIds(ids: List<String>) {
        recentIds.value = ids
    }
}