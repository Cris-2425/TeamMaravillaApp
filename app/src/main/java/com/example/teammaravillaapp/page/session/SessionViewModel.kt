package com.example.teammaravillaapp.page.session

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.teammaravillaapp.data.session.SessionStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel que materializa el estado de sesión global en un único [SessionState] observable.
 *
 * La UI consume un `StateFlow` estable en lugar de combinar manualmente múltiples `Flow` de [SessionStore].
 * Esto reduce complejidad en Compose y evita inconsistencias entre campos (p. ej. `loggedIn` y `username`).
 *
 * ### Regla de seguridad en arranque
 * Si existe una sesión marcada como activa pero `rememberMe == false`, se limpia la sesión al iniciar.
 * Esto previene “auto-login” indeseado tras reinicios cuando el usuario optó por no recordar sesión.
 *
 * ## Concurrencia
 * - Construye [sessionState] con `combine` y lo convierte a `StateFlow` mediante `stateIn`.
 * - `SharingStarted.WhileSubscribed(5_000)` evita trabajo continuo cuando no hay observadores.
 *
 * @property sessionState Estado de sesión listo para consumir en UI (Compose).
 * @constructor Inyecta [SessionStore] como fuente de verdad de sesión.
 *
 * @see SessionStore
 * @see SessionState
 */
@HiltViewModel
class SessionViewModel @Inject constructor(
    private val sessionStore: SessionStore
) : ViewModel() {

    init {
        viewModelScope.launch {
            val loggedIn = sessionStore.isLoggedIn.first()
            val remember = sessionStore.rememberMe.first()

            if (loggedIn && !remember) {
                sessionStore.clearSession()
            }
        }
    }

    /**
     * Estado de sesión derivado de [SessionStore].
     *
     * - `Loading` como valor inicial para evitar estados transitorios durante el primer collect.
     * - `distinctUntilChanged()` para minimizar recomposiciones en UI.
     *
     * @return `StateFlow` con el estado actual de sesión.
     */
    val sessionState: StateFlow<SessionState> =
        combine(
            sessionStore.isLoggedIn,
            sessionStore.rememberMe,
            sessionStore.username
        ) { loggedIn, rememberMe, username ->
            when {
                !loggedIn -> SessionState.LoggedOut
                else -> SessionState.LoggedIn(username = username, canAutoLogin = rememberMe)
            }
        }
            .distinctUntilChanged()
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = SessionState.Loading
            )
}