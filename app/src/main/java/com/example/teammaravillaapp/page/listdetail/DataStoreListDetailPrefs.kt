package com.example.teammaravillaapp.page.listdetail

import com.example.teammaravillaapp.data.local.prefs.user.CategoryFilterPrefs
import com.example.teammaravillaapp.data.local.prefs.user.ListViewTypePrefs
import com.example.teammaravillaapp.model.ListViewType
import com.example.teammaravillaapp.model.ProductCategory
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DataStoreListDetailPrefs @Inject constructor(
    private val categoryFilterPrefs: CategoryFilterPrefs,
    private val listViewTypePrefs: ListViewTypePrefs
) : ListDetailPrefs {

    override fun observeSelectedCategories(): Flow<Set<ProductCategory>> =
        categoryFilterPrefs.observeSelected()

    override fun observeViewType(): Flow<ListViewType> =
        listViewTypePrefs.observe()

    override suspend fun clearCategoryFilter() {
        categoryFilterPrefs.clear()
    }
}