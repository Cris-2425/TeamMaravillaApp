package com.example.teammaravillaapp.data.prefs

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.teammaravillaapp.data.prefs.PrefsKeys.KEY_THEME_MODE
import com.example.teammaravillaapp.model.ThemeMode
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ThemePrefs @Inject constructor(
    @ApplicationContext private val ctx: Context
) {

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