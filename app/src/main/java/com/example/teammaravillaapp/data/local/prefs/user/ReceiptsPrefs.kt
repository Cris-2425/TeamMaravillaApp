package com.example.teammaravillaapp.data.local.prefs.user

import android.content.Context
import android.net.Uri
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.teammaravillaapp.data.local.prefs.datastore.userPrefsDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ReceiptsPrefs @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private fun keyFor(listId: String) = stringPreferencesKey("receipt_uri_$listId")

    suspend fun saveReceiptUri(listId: String, uri: Uri) {
        context.userPrefsDataStore.edit { prefs ->
            prefs[keyFor(listId)] = uri.toString()
        }
    }

    suspend fun clearReceiptUri(listId: String) {
        context.userPrefsDataStore.edit { prefs ->
            prefs.remove(keyFor(listId))
        }
    }
}