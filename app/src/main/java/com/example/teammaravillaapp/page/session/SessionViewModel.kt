package com.example.teammaravillaapp.page.session

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.teammaravillaapp.data.session.SessionStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

/**
 * ViewModel responsable de exponer el estado de sesión global para la UI.
 *
 * Responsabilidades:
 * - Escuchar los flujos de [SessionStore] (loggedIn / rememberMe / username).
 * - Transformarlos en un único [SessionState] estable para el árbol de UI.
 * - Proveer un estado inicial [SessionState.Loading] para evitar valores indeterminados.
 *
 * Motivo:
 * - Centralizar la lógica de sesión en un único punto.
 * - Simplificar composables: consumen 1 flow en vez de 3.
 *
 * @param sessionStore Fuente de verdad de la sesión (persistencia y flujos).
 * Restricciones:
 * - No nulo.
 *
 * @property sessionState Estado observable listo para consumir en Compose.
 *
 * @throws Exception No se lanza explícitamente desde este ViewModel.
 * Posibles fallos técnicos (no propagados directamente):
 * - Errores de lectura de DataStore/IO dentro de [SessionStore] podrían reflejarse como
 *   valores por defecto o emisiones inesperadas según la implementación del Store.
 *
 * @see SessionState Modelo de estado.
 * @see SessionStore Persistencia/observación de sesión.
 *
 * Ejemplo de uso:
 * {@code
 * val vm: SessionViewModel = hiltViewModel()
 * val st by vm.sessionState.collectAsStateWithLifecycle()
 * if (st is SessionState.LoggedOut) { ... }
 * }
 */
@HiltViewModel
class SessionViewModel @Inject constructor(
    private val sessionStore: SessionStore
) : ViewModel() {

    /**
     * Estado global de sesión derivado de [SessionStore].
     *
     * Regla actual:
     * - Si `loggedIn == true` Y `rememberMe == true` ⇒ [SessionState.LoggedIn]
     * - En cualquier otro caso ⇒ [SessionState.LoggedOut]
     *
     * Nota de negocio:
     * - Si “remember me” solo decide persistencia (no si estás logueado ahora),
     *   podrías querer considerar LoggedIn cuando `loggedIn == true` aunque rememberMe sea false.
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