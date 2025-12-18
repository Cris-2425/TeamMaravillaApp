package com.example.teammaravillaapp.page.home

import com.example.teammaravillaapp.model.UserList

data class HomeUiState(
    val search: String = "",
    val recentLists: List<Pair<String, UserList>> = emptyList()
)