package com.example.teammaravillaapp.data.local.prefs.user

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

/**
 * Gestión de preferencias relacionadas con el tema de la aplicación.
 *
 * Persistencia basada en DataStore (Preferences), bajo la clave [PrefKeys.KEY_THEME_MODE].
 *
 * Funcionalidades:
 * - Observar el modo de tema actual mediante Flow.
 * - Cambiar el modo de tema (Light, Dark, System).
 *
 * Uso típico:
 * ```
 * @Inject lateinit var themePrefs: ThemePrefs
 *
 * val currentTheme: Flow<ThemeMode> = themePrefs.themeMode
 * themePrefs.setThemeMode(ThemeMode.DARK)
 * ```
 */
@Singleton
class ThemePrefs @Inject constructor(
    @ApplicationContext private val context: Context
) {

    /** Flujo que emite el modo de tema actual, con valor por defecto [ThemeMode.SYSTEM] */
    val themeMode: Flow<ThemeMode> =
        context.userPrefsDataStore.data
            .map { prefs ->
                val raw = prefs[PrefKeys.KEY_THEME_MODE] ?: ThemeMode.SYSTEM.name
                runCatching { ThemeMode.valueOf(raw) }.getOrElse { ThemeMode.SYSTEM }
            }
            .distinctUntilChanged()

    /**
     * Cambia el modo de tema de la aplicación.
     *
     * @param mode Modo de tema deseado.
     */
    suspend fun setThemeMode(mode: ThemeMode) {
        context.userPrefsDataStore.edit { prefs ->
            prefs[PrefKeys.KEY_THEME_MODE] = mode.name
        }
    }
}