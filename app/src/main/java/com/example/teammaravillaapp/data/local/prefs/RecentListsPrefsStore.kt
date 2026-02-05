package com.example.teammaravillaapp.data.local.prefs

import android.content.Context
import androidx.datastore.preferences.core.edit
import com.example.teammaravillaapp.data.local.prefs.datastore.userPrefsDataStore
import com.example.teammaravillaapp.data.local.prefs.keys.PrefKeys
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import org.json.JSONArray
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RecentListsPrefsStore @Inject constructor(
    @ApplicationContext private val context: Context
) {
    fun observeIds(): Flow<List<String>> =
        context.userPrefsDataStore.data
            .map { prefs ->
                val raw = prefs[PrefKeys.KEY_RECENT_LIST_IDS].orEmpty()
                parseJsonArray(raw)
            }
            .distinctUntilChanged()

    suspend fun push(listId: String, max: Int = 20) {
        context.userPrefsDataStore.edit { prefs ->
            val current = parseJsonArray(prefs[PrefKeys.KEY_RECENT_LIST_IDS].orEmpty())
                .filter { it != listId }
                .toMutableList()

            current.add(0, listId)
            prefs[PrefKeys.KEY_RECENT_LIST_IDS] = toJsonArray(current.take(max))
        }
    }

    suspend fun remove(listId: String) {
        context.userPrefsDataStore.edit { prefs ->
            val current = parseJsonArray(prefs[PrefKeys.KEY_RECENT_LIST_IDS].orEmpty())
                .filter { it != listId }
            prefs[PrefKeys.KEY_RECENT_LIST_IDS] = toJsonArray(current)
        }
    }

    suspend fun clear() {
        context.userPrefsDataStore.edit { prefs ->
            prefs[PrefKeys.KEY_RECENT_LIST_IDS] = "[]"
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