package com.example.teammaravillaapp.data.repository.users

/**
 * Contrato del repositorio de usuarios y autenticación.
 *
 * Define las operaciones de alto nivel relacionadas con:
 * - Inicio de sesión
 * - Registro de usuario
 * - Cierre de sesión
 *
 * ### Responsabilidad
 * - Actuar como punto único de acceso para la autenticación.
 * - Ocultar el origen de los datos (remoto, sesión local, etc.).
 *
 * ### Decisiones de diseño
 * - Expone una API simple basada en `Boolean` para facilitar su consumo
 *   desde ViewModels o UseCases.
 * - Las validaciones básicas se realizan en la implementación concreta.
 *
 * Este contrato no define cómo se almacenan las credenciales ni
 * cómo se comunica con el backend.
 */
interface UsersRepository {

    /**
     * Inicia sesión de un usuario.
     *
     * @param username nombre de usuario ingresado.
     * @param password contraseña en texto plano.
     * @param rememberMe indica si la sesión debe persistir entre reinicios.
     * @return `true` si la autenticación fue exitosa, `false` en caso contrario.
     */
    suspend fun login(
        username: String,
        password: String,
        rememberMe: Boolean
    ): Boolean

    /**
     * Registra un nuevo usuario y crea una sesión activa si el registro es exitoso.
     *
     * @param username nombre de usuario.
     * @param email email del usuario.
     * @param password contraseña en texto plano.
     * @param rememberMe indica si la sesión debe persistir.
     * @return `true` si el registro fue exitoso, `false` si falló.
     */
    suspend fun register(
        username: String,
        email: String,
        password: String,
        rememberMe: Boolean
    ): Boolean

    /**
     * Cierra la sesión activa del usuario.
     *
     * Elimina cualquier información de sesión persistida localmente.
     */
    suspend fun logout()
}