package com.example.teammaravillaapp.page.history

data class HistoryUiState(
    val isLoading: Boolean = true,
    val rows: List<HistoryRow> = emptyList()
)

data class HistoryRow(
    val id: String,
    val name: String
)