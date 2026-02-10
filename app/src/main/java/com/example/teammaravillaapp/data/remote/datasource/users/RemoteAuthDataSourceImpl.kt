package com.example.teammaravillaapp.data.remote.datasource.users

import com.example.teammaravillaapp.data.remote.api.AuthApi
import com.example.teammaravillaapp.data.remote.dto.AuthResponseDto
import com.example.teammaravillaapp.data.remote.dto.LoginRequestDto
import com.example.teammaravillaapp.data.remote.dto.RegisterRequestDto
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Implementación concreta de [RemoteAuthDataSource] usando Retrofit ([AuthApi]).
 *
 * Se encarga de:
 * - Construir los DTOs de request.
 * - Llamar a la API.
 * - Devolver el DTO de respuesta.
 *
 * Anotado con [Singleton] para garantizar una única instancia
 * en toda la aplicación mediante Hilt.
 *
 * @property api Interfaz de Retrofit para llamadas de autenticación.
 */
@Singleton
class RemoteAuthDataSourceImpl @Inject constructor(
    private val api: AuthApi
) : RemoteAuthDataSource {

    override suspend fun login(name: String, password: String): AuthResponseDto =
        api.login(LoginRequestDto(name = name, passwd = password))

    override suspend fun register(name: String, email: String, password: String): AuthResponseDto =
        api.register(RegisterRequestDto(name = name, passwd = password, email = email))
}