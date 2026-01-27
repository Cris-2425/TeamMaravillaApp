package com.example.teammaravillaapp.page.home

import com.example.teammaravillaapp.model.UserList
import com.example.teammaravillaapp.data.repository.ListProgress

data class HomeUiState(
    val search: String = "",
    val recentLists: List<HomeListRow> = emptyList()
)

data class HomeListRow(
    val id: String,
    val list: UserList,
    val progress: ListProgress = ListProgress(checkedCount = 0, totalCount = 0)
)