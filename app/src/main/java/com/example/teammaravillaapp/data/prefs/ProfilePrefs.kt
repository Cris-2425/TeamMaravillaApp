package com.example.teammaravillaapp.data.prefs

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val KEY_PROFILE_PHOTO = stringPreferencesKey("profile_photo_uri")

fun profilePhotoFlow(ctx: Context): Flow<String?> =
    ctx.userPrefsDataStore.data.map { it[KEY_PROFILE_PHOTO] }

suspend fun saveProfilePhoto(ctx: Context, uri: String) {
    ctx.userPrefsDataStore.edit { prefs ->
        prefs[KEY_PROFILE_PHOTO] = uri
    }
}

suspend fun clearProfilePhoto(ctx: Context) {
    ctx.userPrefsDataStore.edit { prefs ->
        prefs.remove(KEY_PROFILE_PHOTO)
    }
}