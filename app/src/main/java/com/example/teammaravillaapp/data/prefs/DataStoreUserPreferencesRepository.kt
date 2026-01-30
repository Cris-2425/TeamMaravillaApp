package com.example.teammaravillaapp.data.prefs

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.stringSetPreferencesKey
import com.example.teammaravillaapp.model.ListStyle
import com.example.teammaravillaapp.model.ProductCategory
import com.example.teammaravillaapp.repository.UserPrefsRepository
import com.example.teammaravillaapp.util.TAG_GLOBAL
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class DataStoreUserPrefsRepository @Inject constructor(
    private val dataStore: DataStore<Preferences>
) : UserPrefsRepository {

    private object Keys {
        val LIST_STYLE = stringPreferencesKey("list_style")
        val HIDDEN_CATEGORIES = stringSetPreferencesKey("hidden_categories")
    }

    override val listStyle: Flow<ListStyle> =
        dataStore.data
            .map { prefs ->
                val raw = prefs[Keys.LIST_STYLE] ?: ListStyle.LISTA.name
                runCatching { ListStyle.valueOf(raw) }.getOrElse { ListStyle.LISTA }
            }
            .distinctUntilChanged()

    override val hiddenCategories: Flow<Set<ProductCategory>> =
        dataStore.data
            .map { prefs ->
                prefs[Keys.HIDDEN_CATEGORIES]
                    .orEmpty()
                    .mapNotNull { runCatching { ProductCategory.valueOf(it) }.getOrNull() }
                    .toSet()
            }
            .distinctUntilChanged()

    override val categoryVisibility: Flow<Map<ProductCategory, Boolean>> =
        hiddenCategories
            .map { hidden ->
                ProductCategory.entries.associateWith { it !in hidden }
            }
            .distinctUntilChanged()

    override suspend fun setListStyle(style: ListStyle) {
        dataStore.edit { prefs ->
            prefs[Keys.LIST_STYLE] = style.name
        }
        Log.d(TAG_GLOBAL, "Prefs(DataStore) → listStyle = $style")
    }

    override suspend fun setHiddenCategories(hidden: Set<ProductCategory>) {
        dataStore.edit { prefs ->
            prefs[Keys.HIDDEN_CATEGORIES] = hidden.map { it.name }.toSet()
        }
        Log.d(TAG_GLOBAL, "Prefs(DataStore) → hiddenCategories = $hidden")
    }

    override suspend fun toggleCategory(category: ProductCategory) {
        dataStore.edit { prefs ->
            val current = prefs[Keys.HIDDEN_CATEGORIES].orEmpty().toMutableSet()
            if (current.contains(category.name)) current.remove(category.name)
            else current.add(category.name)
            prefs[Keys.HIDDEN_CATEGORIES] = current
        }
        Log.d(TAG_GLOBAL, "Prefs(DataStore) → toggleCategory = $category")
    }
}