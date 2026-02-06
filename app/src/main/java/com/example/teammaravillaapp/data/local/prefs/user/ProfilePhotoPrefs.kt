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

@Singleton
class ProfilePhotoPrefs @Inject constructor(
    @ApplicationContext private val context: Context
) {
    fun observeUri(): Flow<String?> =
        context.userPrefsDataStore.data
            .map { prefs -> prefs[PrefKeys.KEY_PROFILE_PHOTO] }

    suspend fun saveUri(uriString: String) {
        context.userPrefsDataStore.edit { prefs ->
            prefs[PrefKeys.KEY_PROFILE_PHOTO] = uriString
        }
    }

    suspend fun clear() {
        context.userPrefsDataStore.edit { prefs ->
            prefs.remove(PrefKeys.KEY_PROFILE_PHOTO)
        }
    }
}