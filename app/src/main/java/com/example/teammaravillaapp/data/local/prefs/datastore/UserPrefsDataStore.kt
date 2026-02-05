package com.example.teammaravillaapp.data.local.prefs.datastore

import android.content.Context
import androidx.datastore.preferences.preferencesDataStore

val Context.userPrefsDataStore by preferencesDataStore(name = "user_prefs")
