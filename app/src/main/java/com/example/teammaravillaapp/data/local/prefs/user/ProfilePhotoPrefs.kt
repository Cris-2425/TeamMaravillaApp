package com.example.teammaravillaapp.data.local.prefs.user

import android.content.Context
import androidx.datastore.preferences.core.edit
import com.example.teammaravillaapp.data.local.prefs.datastore.userPrefsDataStore
import com.example.teammaravillaapp.data.local.prefs.keys.PrefKeys
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Capa de acceso a DataStore para persistir y observar la foto de perfil del usuario.
 *
 * Funcionalidades:
 * - Flujo de observación de la URI de la foto de perfil.
 * - Guardar y limpiar la foto de perfil.
 *
 * Uso típico desde ViewModel:
 * ```
 * val photoUriFlow: Flow<String?> = profilePhotoPrefs.observeUri()
 * ```
 */
@Singleton
class ProfilePhotoPrefs @Inject constructor(
    @ApplicationContext private val context: Context
) {

    /** Flujo que emite la URI actual de la foto de perfil, o null si no hay. */
    fun observeUri(): Flow<String?> =
        context.userPrefsDataStore.data
            .map { prefs -> prefs[PrefKeys.KEY_PROFILE_PHOTO] }

    /** Guarda la URI de la foto de perfil. */
    suspend fun saveUri(uriString: String) {
        context.userPrefsDataStore.edit { prefs ->
            prefs[PrefKeys.KEY_PROFILE_PHOTO] = uriString
        }
    }

    /** Limpia la URI de la foto de perfil almacenada. */
    suspend fun clear() {
        context.userPrefsDataStore.edit { prefs ->
            prefs.remove(PrefKeys.KEY_PROFILE_PHOTO)
        }
    }
}