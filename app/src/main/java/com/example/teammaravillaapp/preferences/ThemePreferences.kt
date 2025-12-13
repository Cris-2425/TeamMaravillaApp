package com.example.teammaravillaapp.preferences

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.map

val Context.dataStore by preferencesDataStore(name = "settings")

object ThemeKeys {
    val THEME_MODE = intPreferencesKey("theme_mode")
}

enum class ThemeMode(val value: Int) {
    LIGHT(0),
    DARK(1),
    SYSTEM(2);

    companion object {
        fun fromValue(value: Int) = entries.firstOrNull { it.value == value } ?: SYSTEM
    }
}
