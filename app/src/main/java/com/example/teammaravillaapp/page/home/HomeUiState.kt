package com.example.teammaravillaapp.page.home

import com.example.teammaravillaapp.data.repository.lists.ListProgress
import com.example.teammaravillaapp.model.UserList

data class HomeUiState(
    val search: String = "",
    val rows: List<HomeListRow> = emptyList()
)

data class HomeListRow(
    val id: String,
    val list: UserList,
    val progress: ListProgress = ListProgress(checkedCount = 0, totalCount = 0)
)
