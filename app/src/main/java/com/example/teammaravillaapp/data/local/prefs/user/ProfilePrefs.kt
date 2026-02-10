package com.example.teammaravillaapp.data.local.prefs.user

import android.content.Context
import androidx.datastore.preferences.core.edit
import com.example.teammaravillaapp.data.local.prefs.datastore.userPrefsDataStore
import com.example.teammaravillaapp.data.local.prefs.keys.PrefKeys.KEY_PROFILE_PHOTO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * Flujo utilitario para observar la URI de la foto de perfil desde cualquier contexto.
 *
 * @param ctx Contexto de la aplicación.
 * @return Flujo que emite la URI actual o null si no hay.
 */
fun profilePhotoFlow(ctx: Context): Flow<String?> =
    ctx.userPrefsDataStore.data.map { it[KEY_PROFILE_PHOTO] }

/** Guarda la URI de la foto de perfil usando funciones de extensión. */
suspend fun saveProfilePhoto(ctx: Context, uri: String) {
    ctx.userPrefsDataStore.edit { prefs ->
        prefs[KEY_PROFILE_PHOTO] = uri
    }
}

/** Limpia la URI de la foto de perfil almacenada. */
suspend fun clearProfilePhoto(ctx: Context) {
    ctx.userPrefsDataStore.edit { prefs ->
        prefs.remove(KEY_PROFILE_PHOTO)
    }
}