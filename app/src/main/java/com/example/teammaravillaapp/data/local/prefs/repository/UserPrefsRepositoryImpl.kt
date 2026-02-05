package com.example.teammaravillaapp.data.local.prefs.repository

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import com.example.teammaravillaapp.data.local.prefs.keys.PrefKeys
import com.example.teammaravillaapp.model.ListStyle
import com.example.teammaravillaapp.model.ProductCategory
import com.example.teammaravillaapp.util.TAG_GLOBAL
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserPrefsRepositoryImpl @Inject constructor(
    private val dataStore: DataStore<Preferences>
) : UserPrefsRepository {

    override val listStyle: Flow<ListStyle> =
        dataStore.data
            .map { prefs ->
                val raw = prefs[PrefKeys.KEY_LIST_STYLE] ?: ListStyle.LISTA.name
                runCatching { ListStyle.valueOf(raw) }.getOrElse { ListStyle.LISTA }
            }
            .distinctUntilChanged()

    override val hiddenCategories: Flow<Set<ProductCategory>> =
        dataStore.data
            .map { prefs ->
                val raw = prefs[PrefKeys.KEY_CATEGORY_FILTER_SET].orEmpty()
                raw.mapNotNull { runCatching { ProductCategory.valueOf(it) }.getOrNull() }.toSet()
            }
            .distinctUntilChanged()

    override val categoryVisibility: Flow<Map<ProductCategory, Boolean>> =
        hiddenCategories
            .map { hidden -> ProductCategory.entries.associateWith { it !in hidden } }
            .distinctUntilChanged()

    override suspend fun setListStyle(style: ListStyle) {
        dataStore.edit { prefs ->
            prefs[PrefKeys.KEY_LIST_STYLE] = style.name
        }
        Log.d(TAG_GLOBAL, "Prefs(DataStore) → listStyle = $style")
    }

    override suspend fun setHiddenCategories(hidden: Set<ProductCategory>) {
        dataStore.edit { prefs ->
            prefs[PrefKeys.KEY_CATEGORY_FILTER_SET] = hidden.map { it.name }.toSet()
        }
        Log.d(TAG_GLOBAL, "Prefs(DataStore) → hiddenCategories = $hidden")
    }

    override suspend fun toggleCategory(category: ProductCategory) {
        dataStore.edit { prefs ->
            val current = prefs[PrefKeys.KEY_CATEGORY_FILTER_SET].orEmpty().toMutableSet()
            if (!current.add(category.name)) current.remove(category.name)
            prefs[PrefKeys.KEY_CATEGORY_FILTER_SET] = current
        }
        Log.d(TAG_GLOBAL, "Prefs(DataStore) → toggleCategory = $category")
    }
}