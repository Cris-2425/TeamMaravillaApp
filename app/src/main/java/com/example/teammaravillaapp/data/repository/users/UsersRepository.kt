package com.example.teammaravillaapp.data.repository.users

/**
 * Contrato de autenticación.
 */

interface UsersRepository {
    suspend fun login(email: String, password: String): Boolean
    suspend fun logout()
}