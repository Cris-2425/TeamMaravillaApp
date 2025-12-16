package com.example.teammaravillaapp.data.auth

/**
 * Contrato de autenticación.
 *
 * Ventaja: permite cambiar la implementación (fake / Retrofit / Room-DataStore)
 * sin tocar LoginViewModel ni la UI.
 */
interface AuthRepository {
    suspend fun login(email: String, password: String): Boolean
    suspend fun logout()
}