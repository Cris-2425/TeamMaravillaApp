package com.example.teammaravillaapp.data.prefs

import android.content.Context
import android.net.Uri
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ReceiptsPrefs @Inject constructor(
    @ApplicationContext private val context: Context
) {
    suspend fun saveReceiptUri(listId: String, uri: Uri) {
        val key = stringPreferencesKey("receipt_uri_$listId")
        context.userPrefsDataStore.edit { prefs ->
            prefs[key] = uri.toString()
        }
    }
}