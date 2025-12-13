package com.example.teammaravillaapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.teammaravillaapp.preferences.ThemeKeys
import com.example.teammaravillaapp.preferences.ThemeMode
import com.example.teammaravillaapp.preferences.dataStore
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import androidx.datastore.preferences.core.edit

open class AppSettingsViewModel(application: Application) : AndroidViewModel(application) {

    open val themeMode: StateFlow<ThemeMode> =
        application.dataStore.data
            .map { prefs -> ThemeMode.fromValue(prefs[ThemeKeys.THEME_MODE] ?: ThemeMode.SYSTEM.value) }
            .stateIn(viewModelScope, SharingStarted.Eagerly, ThemeMode.SYSTEM)

    open fun setThemeMode(mode: ThemeMode) {
        viewModelScope.launch {
            getApplication<Application>().dataStore.edit { prefs ->
                prefs[ThemeKeys.THEME_MODE] = mode.value
            }
        }
    }
}
