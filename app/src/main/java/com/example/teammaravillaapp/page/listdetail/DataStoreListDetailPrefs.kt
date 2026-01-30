package com.example.teammaravillaapp.page.listdetail

import android.content.Context
import com.example.teammaravillaapp.data.prefs.CategoryFilterPrefs
import com.example.teammaravillaapp.data.prefs.ListViewTypePrefs
import com.example.teammaravillaapp.model.ListViewType
import com.example.teammaravillaapp.model.ProductCategory
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DataStoreListDetailPrefs @Inject constructor(
    @ApplicationContext private val context: Context
) : ListDetailPrefs {

    override fun observeSelectedCategories(): Flow<Set<ProductCategory>> =
        CategoryFilterPrefs.observe(context)

    override fun observeViewType(): Flow<ListViewType> =
        ListViewTypePrefs.observe(context)

    override suspend fun clearCategoryFilter() {
        CategoryFilterPrefs.clear(context)
    }
}