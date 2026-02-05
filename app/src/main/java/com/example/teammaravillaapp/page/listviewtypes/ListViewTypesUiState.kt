package com.example.teammaravillaapp.page.listviewtypes

import com.example.teammaravillaapp.model.ListViewType

data class ListViewTypesUiState(
    val isLoading: Boolean = true,
    val selected: ListViewType = ListViewType.BUBBLES
)