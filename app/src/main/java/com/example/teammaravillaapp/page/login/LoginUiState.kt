package com.example.teammaravillaapp.page.login

/**
 * Estado de UI del formulario de login.
 *
 * @property username Usuario o email introducido por el usuario.
 * Restricciones:
 * - Puede ser vacío mientras se escribe.
 * - Se considera válido (para habilitar el botón) cuando no está en blanco.
 * @property password Contraseña introducida por el usuario.
 * Restricciones:
 * - Puede ser vacío mientras se escribe.
 * - Para habilitar el botón se exige longitud mínima (ver [isLoginButtonEnabled]).
 * @property rememberMe Indica si la sesión debe persistirse (p.ej. en DataStore).
 * @property isLoading Indica si hay una operación de login en curso.
 * Restricciones:
 * - Mientras sea true, se recomienda deshabilitar inputs y acciones.
 *
 * Ejemplo:
 * {@code
 * val st = LoginUiState(username = "cris", password = "1234", rememberMe = true)
 * if (st.isLoginButtonEnabled) { /* allow submit */ }
 * }
 */
data class LoginUiState(
    val username: String = "",
    val password: String = "",
    val rememberMe: Boolean = true,
    val isLoading: Boolean = false
) {
    /**
     * Indica si el botón “Iniciar sesión” debe estar habilitado.
     *
     * Criterios:
     * - El usuario no está en blanco.
     * - La contraseña tiene longitud mínima de 4 caracteres.
     * - No hay un login en curso.
     */
    val isLoginButtonEnabled: Boolean
        get() = username.isNotBlank() && password.length >= 4 && !isLoading
}