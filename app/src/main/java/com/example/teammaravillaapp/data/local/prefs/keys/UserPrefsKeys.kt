package com.example.teammaravillaapp.data.local.prefs.keys

import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.stringSetPreferencesKey

object PrefKeys {

    // List view type
    val KEY_LIST_VIEW_TYPE = stringPreferencesKey("list_view_type")

    // Theme
    val KEY_THEME_MODE = stringPreferencesKey("theme_mode")

    // Category filter (set)
    val KEY_CATEGORY_FILTER_SET = stringSetPreferencesKey("category_filter_set")

    // Profile photo
    val KEY_PROFILE_PHOTO = stringPreferencesKey("profile_photo_uri")

    // Recent lists (JSON array string)
    val KEY_RECENT_LIST_IDS = stringPreferencesKey("recent_list_ids_json")

    val KEY_LIST_STYLE = stringPreferencesKey("list_style")

    private val ONBOARDING_COMPLETED = booleanPreferencesKey("onboarding_completed")

}