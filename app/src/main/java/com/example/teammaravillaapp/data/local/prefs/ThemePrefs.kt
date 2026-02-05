package com.example.teammaravillaapp.data.local.prefs

import android.content.Context
import androidx.datastore.preferences.core.edit
import com.example.teammaravillaapp.data.local.prefs.datastore.userPrefsDataStore
import com.example.teammaravillaapp.data.local.prefs.keys.PrefKeys
import com.example.teammaravillaapp.model.ThemeMode
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ThemePrefs @Inject constructor(
    @ApplicationContext private val context: Context
) {
    val themeMode: Flow<ThemeMode> =
        context.userPrefsDataStore.data
            .map { prefs ->
                val raw = prefs[PrefKeys.KEY_THEME_MODE] ?: ThemeMode.SYSTEM.name
                runCatching { ThemeMode.valueOf(raw) }.getOrElse { ThemeMode.SYSTEM }
            }
            .distinctUntilChanged()

    suspend fun setThemeMode(mode: ThemeMode) {
        context.userPrefsDataStore.edit { prefs ->
            prefs[PrefKeys.KEY_THEME_MODE] = mode.name
        }
    }
}