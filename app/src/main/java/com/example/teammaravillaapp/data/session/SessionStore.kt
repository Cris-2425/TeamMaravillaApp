package com.example.teammaravillaapp.data.session

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "session_prefs")

class SessionStore(private val appContext: Context) {

    private object Keys {
        val LOGGED_IN: Preferences.Key<Boolean> = booleanPreferencesKey("logged_in")
        val USERNAME: Preferences.Key<String> = stringPreferencesKey("username")
        val TOKEN: Preferences.Key<String> = stringPreferencesKey("token")
    }

    val isLoggedIn: Flow<Boolean> =
        appContext.dataStore.data.map { prefs -> prefs[Keys.LOGGED_IN] ?: false }

    val username: Flow<String?> =
        appContext.dataStore.data.map { prefs -> prefs[Keys.USERNAME] }

    val token: Flow<String?> =
        appContext.dataStore.data.map { prefs -> prefs[Keys.TOKEN] }

    suspend fun saveSession(username: String, token: String? = null) {
        appContext.dataStore.edit { prefs ->
            prefs[Keys.LOGGED_IN] = true
            prefs[Keys.USERNAME] = username
            if (token != null) prefs[Keys.TOKEN] = token else prefs.remove(Keys.TOKEN)
        }
    }

    suspend fun clearSession() {
        appContext.dataStore.edit { prefs ->
            prefs[Keys.LOGGED_IN] = false
            prefs.remove(Keys.USERNAME)
            prefs.remove(Keys.TOKEN)
        }
    }
}
