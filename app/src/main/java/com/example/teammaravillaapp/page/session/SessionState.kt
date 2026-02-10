package com.example.teammaravillaapp.page.session

import com.example.teammaravillaapp.page.session.SessionViewModel
import com.example.teammaravillaapp.data.session.SessionStore
/**
 * Representa el estado global de sesión de la aplicación.
 *
 * Se usa como contrato entre la capa de sesión (DataStore / SessionStore)
 * y la UI para decidir navegación y comportamiento.
 *
 * Diseño actual:
 * - `loggedIn` decide si hay sesión activa.
 * - `rememberMe` NO decide si estás logueado, solo si se permite auto-login.
 *
 * @see SessionViewModel
 * @see SessionStore
 */
sealed interface SessionState {

    /**
     * Estado inicial mientras se resuelve la sesión real desde DataStore.
     *
     * En UI normalmente se representa con Splash o Loading.
     */
    data object Loading : SessionState

    /**
     * No existe sesión activa.
     *
     * Ocurre cuando:
     * - loggedIn = false
     */
    data object LoggedOut : SessionState

    /**
     * Existe una sesión activa.
     *
     * @property username Nombre de usuario persistido (puede ser null).
     * @property canAutoLogin Indica si la sesión debe restaurarse automáticamente
     * tras reiniciar la app (depende de rememberMe).
     *
     * Importante:
     * - `canAutoLogin` NO decide si estás logueado.
     * - Solo decide comportamiento en arranque (Splash → Home o Login).
     */
    data class LoggedIn(
        val username: String?,
        val canAutoLogin: Boolean
    ) : SessionState
}