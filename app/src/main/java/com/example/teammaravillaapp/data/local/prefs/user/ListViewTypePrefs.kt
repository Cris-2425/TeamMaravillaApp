package com.example.teammaravillaapp.data.local.prefs.user

import android.content.Context
import androidx.datastore.preferences.core.edit
import com.example.teammaravillaapp.data.local.prefs.datastore.userPrefsDataStore
import com.example.teammaravillaapp.data.local.prefs.keys.PrefKeys
import com.example.teammaravillaapp.model.ListViewType
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ListViewTypePrefs @Inject constructor(
    @ApplicationContext private val context: Context
) {
    fun observe(): Flow<ListViewType> =
        context.userPrefsDataStore.data
            .map { prefs ->
                val raw = prefs[PrefKeys.KEY_LIST_VIEW_TYPE]
                runCatching { raw?.let { ListViewType.valueOf(it) } }.getOrNull()
                    ?: ListViewType.BUBBLES
            }
            .distinctUntilChanged()

    suspend fun set(value: ListViewType) {
        context.userPrefsDataStore.edit { prefs ->
            prefs[PrefKeys.KEY_LIST_VIEW_TYPE] = value.name
        }
    }
}