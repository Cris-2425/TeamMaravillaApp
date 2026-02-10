package com.example.teammaravillaapp.ui.app

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.teammaravillaapp.data.local.prefs.user.ThemePrefs
import com.example.teammaravillaapp.model.ThemeMode
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel responsable de la preferencia de tema global.
 *
 * Responsabilidades:
 * - Exponer el tema actual como [StateFlow] para Compose.
 * - Persistir cambios mediante [ThemePrefs].
 *
 * @param prefs Fuente de verdad del modo de tema (DataStore).
 */
@HiltViewModel
class ThemeViewModel @Inject constructor(
    private val prefs: ThemePrefs
) : ViewModel() {

    /**
     * Modo de tema observable por la UI.
     *
     * - `SYSTEM` como valor inicial hasta que DataStore emita.
     */
    val themeMode: StateFlow<ThemeMode> =
        prefs.themeMode.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = ThemeMode.SYSTEM
        )

    /**
     * Actualiza el modo de tema.
     *
     * @param mode Nuevo modo a persistir.
     */
    fun setThemeMode(mode: ThemeMode) {
        viewModelScope.launch { prefs.setThemeMode(mode) }
    }
}