package com.example.teammaravillaapp.data.prefs

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.teammaravillaapp.model.ThemeMode
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val KEY_THEME_MODE = stringPreferencesKey("theme_mode")

class ThemePrefs(private val ctx: Context) {

    val themeMode: Flow<ThemeMode> =
        ctx.userPrefsDataStore.data.map { prefs ->
            val raw = prefs[KEY_THEME_MODE] ?: ThemeMode.SYSTEM.name
            runCatching { ThemeMode.valueOf(raw) }.getOrElse { ThemeMode.SYSTEM }
        }

    suspend fun setThemeMode(mode: ThemeMode) {
        ctx.userPrefsDataStore.edit { prefs ->
            prefs[KEY_THEME_MODE] = mode.name
        }
    }
}