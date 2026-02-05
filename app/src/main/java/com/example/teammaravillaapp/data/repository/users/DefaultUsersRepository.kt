package com.example.teammaravillaapp.data.repository.users

import com.example.teammaravillaapp.data.remote.datasource.users.RemoteUsersDataSource
import com.example.teammaravillaapp.data.session.SessionStore
import com.example.teammaravillaapp.data.remote.dto.UserRemoteDto
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DefaultUsersRepository @Inject constructor(
    private val sessionStore: SessionStore,
    private val remoteUsers: RemoteUsersDataSource
) : UsersRepository {

    override suspend fun login(email: String, password: String): Boolean {
        val cleanEmail = email.trim()
        if (cleanEmail.isBlank() || password.length < 4) return false

        val userId = userIdFromEmail(cleanEmail)

        val existing = remoteUsers.getUser(userId)

        val ok = if (existing == null) {
            // crear usuario
            remoteUsers.saveUser(
                UserRemoteDto(
                    id = userId,
                    email = cleanEmail,
                    password = password,
                    name = cleanEmail,
                    surname = "",
                    age = 0,
                    createdAt = System.currentTimeMillis()
                )
            )
            true
        } else {
            existing.password == password
        }

        if (ok) {
            // guardamos userId dentro de token (mínimo cambio)
            sessionStore.saveSession(username = cleanEmail, token = userId)
        }

        return ok
    }

    override suspend fun logout() {
        sessionStore.clearSession()
    }

    private fun userIdFromEmail(email: String): String =
        "u_" + email.lowercase()
            .replace("@", "_at_")
            .replace(".", "_")
            .replace(Regex("[^a-z0-9_]+"), "_")
}