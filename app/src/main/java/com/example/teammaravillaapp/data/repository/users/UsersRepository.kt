package com.example.teammaravillaapp.data.repository.users

/**
 * Contrato de autenticación.
 */

interface UsersRepository {
    suspend fun login(username: String, password: String, rememberMe: Boolean): Boolean
    suspend fun register(
        username: String,
        email: String,
        password: String,
        rememberMe: Boolean
    ): Boolean

    suspend fun logout()

}