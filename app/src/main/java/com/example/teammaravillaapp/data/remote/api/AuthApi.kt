package com.example.teammaravillaapp.data.remote.api

import com.example.teammaravillaapp.data.remote.dto.AuthResponseDto
import com.example.teammaravillaapp.data.remote.dto.LoginRequestDto
import com.example.teammaravillaapp.data.remote.dto.RegisterRequestDto
import retrofit2.http.Body
import retrofit2.http.POST

/**
 * API para autenticación de usuarios.
 *
 * Proporciona endpoints para login y registro.
 */
interface AuthApi {

    /**
     * Inicia sesión con un nombre de usuario y contraseña.
     *
     * @param request Objeto con las credenciales de login.
     * @return Información del usuario autenticado y mensaje del backend.
     */
    @POST("auth/login")
    suspend fun login(
        @Body request: LoginRequestDto
    ): AuthResponseDto

    /**
     * Registra un nuevo usuario.
     *
     * @param request Objeto con los datos de registro (nombre, email, contraseña).
     * @return Información del usuario registrado y mensaje del backend.
     */
    @POST("auth/register")
    suspend fun register(
        @Body request: RegisterRequestDto
    ): AuthResponseDto
}