package com.example.teammaravillaapp.data.auth

import com.example.teammaravillaapp.data.session.SessionStore
import kotlinx.coroutines.delay
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Implementación fake pero realista.
 */

@Singleton
class FakeAuthRepository @Inject constructor(
    private val sessionStore: SessionStore,
) : AuthRepository {

    private val delayMs: Long = 500L

    override suspend fun login(email: String, password: String): Boolean {
        delay(delayMs)

        val ok = (email == "juan" && password == "1234") ||
                (email.isNotBlank() && password.length >= 4)

        if (ok) {
            sessionStore.saveSession(username = email, token = null)
        }
        return ok
    }

    override suspend fun logout() {
        delay(150L)
        sessionStore.clearSession()
    }
}