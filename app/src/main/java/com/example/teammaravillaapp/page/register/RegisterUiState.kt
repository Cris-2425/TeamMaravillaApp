package com.example.teammaravillaapp.page.register

/**
 * Estado de UI del formulario de registro.
 *
 * @property username Nombre de usuario.
 * Restricciones:
 * - Puede ser vacío mientras se escribe.
 * - Se considera válido (para habilitar el botón) cuando no está en blanco.
 * @property email Email del usuario.
 * Restricciones:
 * - Puede ser vacío mientras se escribe.
 * - Se considera válido (para habilitar el botón) cuando no está en blanco.
 * - Si el proyecto exige validación de formato, debería aplicarse en ViewModel.
 * @property password Contraseña del usuario.
 * Restricciones:
 * - Puede ser vacío mientras se escribe.
 * - Para habilitar el botón se exige longitud mínima (ver [isRegisterEnabled]).
 * @property rememberMe Indica si la sesión debe persistirse tras registrarse (p.ej. DataStore).
 * @property isLoading Indica si hay una operación de registro en curso.
 * Restricciones:
 * - Mientras sea true, se recomienda deshabilitar inputs y acciones.
 *
 * Ejemplo:
 * {@code
 * val st = RegisterUiState(username = "cris", email = "a@b.com", password = "1234")
 * if (st.isRegisterEnabled) { /* allow submit */ }
 * }
 */
data class RegisterUiState(
    val username: String = "",
    val email: String = "",
    val password: String = "",
    val rememberMe: Boolean = true,
    val isLoading: Boolean = false
) {
    /**
     * Indica si el botón “Crear cuenta” debe estar habilitado.
     *
     * Criterios:
     * - El usuario no está en blanco.
     * - El email no está en blanco.
     * - La contraseña tiene longitud mínima de 4 caracteres.
     * - No hay un registro en curso.
     */
    val isRegisterEnabled: Boolean
        get() = username.isNotBlank() && email.isNotBlank() && password.length >= 4 && !isLoading
}