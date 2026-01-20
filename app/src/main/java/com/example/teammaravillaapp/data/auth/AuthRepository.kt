package com.example.teammaravillaapp.data.auth

/**
 * Contrato de autenticación.
 */

interface AuthRepository {
    suspend fun login(email: String, password: String): Boolean
    suspend fun logout()
}