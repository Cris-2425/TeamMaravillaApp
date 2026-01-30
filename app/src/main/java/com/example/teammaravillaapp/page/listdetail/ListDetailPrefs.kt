package com.example.teammaravillaapp.page.listdetail

import com.example.teammaravillaapp.model.ListViewType
import com.example.teammaravillaapp.model.ProductCategory
import kotlinx.coroutines.flow.Flow

interface ListDetailPrefs {
    fun observeSelectedCategories(): Flow<Set<ProductCategory>>
    fun observeViewType(): Flow<ListViewType>

    suspend fun clearCategoryFilter()
}