package com.example.teammaravillaapp.data.repository.users

import com.example.teammaravillaapp.data.remote.datasource.users.RemoteAuthDataSource
import com.example.teammaravillaapp.data.session.SessionStore
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DefaultUsersRepository @Inject constructor(
    private val sessionStore: SessionStore,
    private val remoteAuth: RemoteAuthDataSource
) : UsersRepository {

    override suspend fun login(username: String, password: String, rememberMe: Boolean): Boolean {
        val cleanName = username.trim()
        if (cleanName.isBlank() || password.length < 4) return false

        val res = runCatching { remoteAuth.login(cleanName, password) }.getOrNull()
            ?: return false

        // Regla simple y robusta: si no hay id => no hay login
        if (res.id.isBlank()) return false

        // Si tu backend devuelve message de error, úsalo solo como extra
        if (res.message.isAuthErrorMessage()) return false

        sessionStore.saveSession(
            username = res.name.ifBlank { cleanName },
            token = res.id,
            rememberMe = rememberMe
        )
        return true
    }

    override suspend fun register(username: String, email: String, password: String, rememberMe: Boolean): Boolean {
        val cleanName = username.trim()
        val cleanEmail = email.trim()
        if (cleanName.isBlank() || cleanEmail.isBlank() || password.length < 4) return false

        val res = runCatching { remoteAuth.register(cleanName, cleanEmail, password) }.getOrNull()
            ?: return false

        if (res.id.isBlank()) return false
        if (res.message.isAuthErrorMessage()) return false

        sessionStore.saveSession(
            username = res.name.ifBlank { cleanName },
            token = res.id,
            rememberMe = rememberMe
        )
        return true
    }

    override suspend fun logout() {
        sessionStore.clearSession()
    }
}

/**
 * Heurística opcional: úsala solo si tu backend mete mensajes claros de error en `message`.
 * Si da problemas, bórrala y deja solo `id.isBlank()`.
 */
private fun String?.isAuthErrorMessage(): Boolean {
    val m = this?.trim()?.lowercase().orEmpty()
    if (m.isBlank()) return false
    return listOf(
        "error", "invalid", "incorrect", "unauthorized", "forbidden", "fail",
        "wrong", "not found", "doesn't exist", "does not exist"
    ).any { it in m }
}