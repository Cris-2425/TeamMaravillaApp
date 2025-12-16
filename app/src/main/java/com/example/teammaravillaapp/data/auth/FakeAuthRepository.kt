import com.example.teammaravillaapp.data.auth.AuthRepository
import com.example.teammaravillaapp.data.session.SessionStore
import kotlinx.coroutines.delay

/**
 * Implementación "fake" pero realista:
 * - Simula latencia de red
 * - Valida credenciales simples
 * - Puedes ampliarlo para usar Retrofit + persistencia sin cambiar el contrato
 */
class FakeAuthRepository(
    private val sessionStore: SessionStore,
    private val delayMs: Long = 500L
) : AuthRepository {

    override suspend fun login(email: String, password: String): Boolean {
        delay(delayMs)

        val ok = (email == "juan" && password == "1234") ||
                (email.isNotBlank() && password.length >= 4)

        if (ok) {
            // ✅ Persistimos sesión en DataStore
            sessionStore.saveSession(username = email, token = null)
        }
        return ok
    }

    override suspend fun logout() {
        delay(150L)
        // ✅ Limpia sesión persistida
        sessionStore.clearSession()
    }
}