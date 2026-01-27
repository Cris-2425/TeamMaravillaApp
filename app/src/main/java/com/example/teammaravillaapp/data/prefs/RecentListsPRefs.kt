package com.example.teammaravillaapp.data.prefs

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.json.JSONArray

private val KEY_RECENT_LIST_IDS = stringPreferencesKey("recent_list_ids")

object RecentListsPrefs {

    fun observeIds(ctx: Context): Flow<List<String>> =
        ctx.userPrefsDataStore.data.map { prefs ->
            val raw = prefs[KEY_RECENT_LIST_IDS].orEmpty()
            parseJsonArray(raw)
        }

    suspend fun push(ctx: Context, listId: String, max: Int = 20) {
        ctx.userPrefsDataStore.edit { prefs ->
            val current = parseJsonArray(prefs[KEY_RECENT_LIST_IDS].orEmpty())
                .filter { it != listId } // evita duplicados
                .toMutableList()

            current.add(0, listId)      // al principio = más reciente
            val trimmed = current.take(max)

            prefs[KEY_RECENT_LIST_IDS] = toJsonArray(trimmed)
        }
    }

    suspend fun remove(ctx: Context, listId: String) {
        ctx.userPrefsDataStore.edit { prefs ->
            val current = parseJsonArray(prefs[KEY_RECENT_LIST_IDS].orEmpty())
                .filter { it != listId }
            prefs[KEY_RECENT_LIST_IDS] = toJsonArray(current)
        }
    }

    suspend fun clear(ctx: Context) {
        ctx.userPrefsDataStore.edit { prefs ->
            prefs[KEY_RECENT_LIST_IDS] = "[]"
        }
    }

    private fun parseJsonArray(raw: String): List<String> {
        if (raw.isBlank()) return emptyList()
        return runCatching {
            val arr = JSONArray(raw)
            buildList(arr.length()) {
                for (i in 0 until arr.length()) add(arr.getString(i))
            }.filter { it.isNotBlank() }
        }.getOrElse { emptyList() }
    }

    private fun toJsonArray(list: List<String>): String =
        JSONArray(list).toString()
}