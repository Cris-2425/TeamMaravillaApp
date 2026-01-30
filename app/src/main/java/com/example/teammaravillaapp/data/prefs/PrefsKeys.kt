package com.example.teammaravillaapp.data.prefs

import androidx.datastore.preferences.core.stringPreferencesKey

object PrefsKeys {
    val LIST_VIEW_TYPE = stringPreferencesKey("list_view_type")
    val KEY_RECENT_LIST_IDS = stringPreferencesKey("recent_list_ids")
    val KEY_THEME_MODE = stringPreferencesKey("theme_mode")
}