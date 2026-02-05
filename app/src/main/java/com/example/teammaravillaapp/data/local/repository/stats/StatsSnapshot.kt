package com.example.teammaravillaapp.data.local.repository.stats

import com.example.teammaravillaapp.page.stats.TopProductStat

data class StatsSnapshot(
    val lists: Int,
    val products: Int,
    val recipes: Int,
    val favorites: Int,
    val totalItems: Int,
    val checkedItems: Int,
    val listsLast7Days: Int,
    val itemsLast7Days: Int,
    val topProducts: List<TopProductStat>
)