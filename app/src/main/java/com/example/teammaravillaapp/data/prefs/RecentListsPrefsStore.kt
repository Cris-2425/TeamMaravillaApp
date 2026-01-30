package com.example.teammaravillaapp.data.prefs

import android.content.Context
import androidx.datastore.preferences.core.edit
import com.example.teammaravillaapp.data.prefs.PrefsKeys.KEY_RECENT_LIST_IDS
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.json.JSONArray
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RecentListsPrefsStore @Inject constructor(
    @ApplicationContext private val ctx: Context
) {
    fun observeIds(): Flow<List<String>> =
        ctx.userPrefsDataStore.data.map { prefs ->
            val raw = prefs[KEY_RECENT_LIST_IDS].orEmpty()
            parseJsonArray(raw)
        }

    suspend fun push(listId: String, max: Int = 20) {
        ctx.userPrefsDataStore.edit { prefs ->
            val current = parseJsonArray(prefs[KEY_RECENT_LIST_IDS].orEmpty())
                .filter { it != listId }
                .toMutableList()

            current.add(0, listId)
            prefs[KEY_RECENT_LIST_IDS] = toJsonArray(current.take(max))
        }
    }

    suspend fun remove(listId: String) {
        ctx.userPrefsDataStore.edit { prefs ->
            val current = parseJsonArray(prefs[KEY_RECENT_LIST_IDS].orEmpty())
                .filter { it != listId }
            prefs[KEY_RECENT_LIST_IDS] = toJsonArray(current)
        }
    }

    suspend fun clear() {
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