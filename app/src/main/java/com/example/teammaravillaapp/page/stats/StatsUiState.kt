package com.example.teammaravillaapp.page.stats

data class StatsUiState(
    val lists: Int = 0,
    val products: Int = 0,
    val recipes: Int = 0,
    val favorites: Int = 0,

    val totalItems: Int = 0,
    val checkedItems: Int = 0,

    val listsLast7Days: Int = 0,
    val itemsLast7Days: Int = 0,

    val topProducts: List<TopProductStat> = emptyList()
)

data class TopProductStat(
    val name: String,
    val times: Int
)