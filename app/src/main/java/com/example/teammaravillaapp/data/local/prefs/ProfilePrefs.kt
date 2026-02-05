package com.example.teammaravillaapp.data.local.prefs

import android.content.Context
import androidx.datastore.preferences.core.edit
import com.example.teammaravillaapp.data.local.prefs.datastore.userPrefsDataStore
import com.example.teammaravillaapp.data.local.prefs.keys.PrefKeys.KEY_PROFILE_PHOTO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlin.collections.get

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