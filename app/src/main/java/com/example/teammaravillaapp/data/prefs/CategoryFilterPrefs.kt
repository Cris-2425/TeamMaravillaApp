package com.example.teammaravillaapp.data.prefs

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringSetPreferencesKey
import com.example.teammaravillaapp.model.ProductCategory
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

object CategoryFilterPrefs {

    private val KEY = stringSetPreferencesKey("category_filter_set")

    fun observe(context: Context): Flow<Set<ProductCategory>> =
        context.userPrefsDataStore.data.map { prefs ->
            val raw = prefs[KEY] ?: emptySet()
            raw.mapNotNull { name -> runCatching { ProductCategory.valueOf(name) }.getOrNull() }.toSet()
        }

    suspend fun save(context: Context, selected: Set<ProductCategory>) {
        context.userPrefsDataStore.edit { prefs ->
            prefs[KEY] = selected.map { it.name }.toSet()
        }
    }

    suspend fun clear(context: Context) {
        context.userPrefsDataStore.edit { prefs ->
            prefs[KEY] = emptySet()
        }
    }
}