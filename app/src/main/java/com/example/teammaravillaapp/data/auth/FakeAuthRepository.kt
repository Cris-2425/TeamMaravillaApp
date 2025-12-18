package com.example.teammaravillaapp.data.auth

import com.example.teammaravillaapp.data.session.SessionStore
import kotlinx.coroutines.delay

class FakeAuthRepository(
    private val sessionStore: SessionStore,
    private val delayMs: Long = 500L
) : AuthRepository {

    override suspend fun login(email: String, password: String): Boolean {
        delay(delayMs)

        val ok = (email == "juan" && password == "1234") ||
                (email.isNotBlank() && password.length >= 4)

        if (ok) {
            sessionStore.saveSession(username = email, token = null)
        }
        return ok
    }
//
    override suspend fun logout() {
        delay(150L)
        sessionStore.clearSession()
    }
}