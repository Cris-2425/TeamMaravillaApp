package com.example.teammaravillaapp.data.remote.datasource.users

import com.example.teammaravillaapp.data.remote.dto.AuthResponseDto
import com.example.teammaravillaapp.data.remote.api.AuthApi
/**
 * Fuente de datos remota para autenticación de usuarios.
 *
 * Define los contratos para login y registro que se implementan en la capa de red
 * mediante llamadas a la API REST ([AuthApi]).
 *
 * Implementaciones concretas deben encargarse de:
 * - Llamadas HTTP.
 * - Conversión a DTOs.
 * - Manejo de errores (opcional: envoltorio Result en capas superiores).
 */
interface RemoteAuthDataSource {

    /**
     * Realiza el login del usuario contra el backend.
     *
     * @param name Nombre de usuario.
     * @param password Contraseña del usuario.
     * @return [AuthResponseDto] con token, nombre, email y mensaje de la respuesta.
     */
    suspend fun login(name: String, password: String): AuthResponseDto

    /**
     * Registra un nuevo usuario en el backend.
     *
     * @param name Nombre de usuario.
     * @param email Correo electrónico.
     * @param password Contraseña.
     * @return [AuthResponseDto] con token, nombre, email y mensaje de la respuesta.
     */
    suspend fun register(name: String, email: String, password: String): AuthResponseDto
}