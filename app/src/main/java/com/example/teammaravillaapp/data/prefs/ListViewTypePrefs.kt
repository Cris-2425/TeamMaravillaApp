package com.example.teammaravillaapp.data.prefs

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.teammaravillaapp.model.ListViewType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

object ListViewTypePrefs {

    private val KEY = stringPreferencesKey("list_view_type")

    fun observe(ctx: Context): Flow<ListViewType> =
        ctx.userPrefsDataStore.data.map { prefs ->
            val raw = prefs[KEY]
            runCatching { raw?.let { ListViewType.valueOf(it) } }.getOrNull()
                ?: ListViewType.BUBBLES
        }

    suspend fun set(ctx: Context, type: ListViewType) {
        ctx.userPrefsDataStore.edit { prefs ->
            prefs[KEY] = type.name
        }
    }
}