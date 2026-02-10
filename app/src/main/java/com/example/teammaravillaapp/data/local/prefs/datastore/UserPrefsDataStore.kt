package com.example.teammaravillaapp.data.local.prefs.datastore

import android.content.Context
import androidx.datastore.preferences.preferencesDataStore

/**
 * Extensión de Context para inicializar DataStore de preferencias de usuario.
 *
 * - Nombre del DataStore: `"user_prefs"`.
 * - Permite acceder a la instancia de [DataStore<Preferences>] de forma perezosa.
 *
 * Ejemplo de uso:
 * {@code
 * val prefs = context.userPrefsDataStore
 * }
 */
val Context.userPrefsDataStore by preferencesDataStore(name = "user_prefs")