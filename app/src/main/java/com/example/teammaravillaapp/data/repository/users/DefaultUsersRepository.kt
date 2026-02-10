package com.example.teammaravillaapp.data.repository.users

import com.example.teammaravillaapp.data.remote.datasource.users.RemoteAuthDataSource
import com.example.teammaravillaapp.data.session.SessionStore
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Implementación por defecto de [UsersRepository].
 *
 * Orquesta la autenticación remota y la persistencia local de la sesión.
 *
 * ### Responsabilidades
 * - Validar entradas mínimas antes de llamar al backend.
 * - Delegar autenticación y registro al datasource remoto.
 * - Persistir la sesión usando [SessionStore].
 *
 * ### Características
 * - Implementación defensiva: ante cualquier inconsistencia, falla de forma segura.
 * - No lanza excepciones hacia capas superiores.
 * - Adecuada para flujos simples de autenticación en UI.
 */
@Singleton
class DefaultUsersRepository @Inject constructor(
    private val sessionStore: SessionStore,
    private val remoteAuth: RemoteAuthDataSource
) : UsersRepository {

    /**
     * Inicia sesión contra el backend remoto.
     *
     * ### Flujo
     * 1. Normaliza y valida las credenciales.
     * 2. Ejecuta la llamada remota de login.
     * 3. Verifica consistencia de la respuesta (ID válido).
     * 4. Persiste la sesión si el login fue exitoso.
     *
     * @return `true` si el login fue válido y la sesión se guardó correctamente.
     */
    override suspend fun login(
        username: String,
        password: String,
        rememberMe: Boolean
    ): Boolean {
        val cleanName = username.trim()
        if (cleanName.isBlank() || password.length < 4) return false

        val res = runCatching {
            remoteAuth.login(cleanName, password)
        }.getOrNull() ?: return false

        // Regla principal: sin ID no hay sesión válida
        if (res.id.isBlank()) return false

        // Validación adicional basada en mensajes del backend (opcional)
        if (res.message.isAuthErrorMessage()) return false

        sessionStore.saveSession(
            username = res.name.ifBlank { cleanName },
            token = res.id,
            rememberMe = rememberMe
        )
        return true
    }

    /**
     * Registra un nuevo usuario en el backend y crea una sesión local.
     *
     * ### Flujo
     * - Valida campos mínimos.
     * - Ejecuta el registro remoto.
     * - Verifica consistencia de la respuesta.
     * - Persiste la sesión local.
     *
     * @return `true` si el registro fue exitoso.
     */
    override suspend fun register(
        username: String,
        email: String,
        password: String,
        rememberMe: Boolean
    ): Boolean {
        val cleanName = username.trim()
        val cleanEmail = email.trim()
        if (cleanName.isBlank() || cleanEmail.isBlank() || password.length < 4) return false

        val res = runCatching {
            remoteAuth.register(cleanName, cleanEmail, password)
        }.getOrNull() ?: return false

        if (res.id.isBlank()) return false
        if (res.message.isAuthErrorMessage()) return false

        sessionStore.saveSession(
            username = res.name.ifBlank { cleanName },
            token = res.id,
            rememberMe = rememberMe
        )
        return true
    }

    /**
     * Cierra la sesión actual eliminando cualquier estado persistido.
     *
     * No realiza llamadas remotas.
     */
    override suspend fun logout() {
        sessionStore.clearSession()
    }
}

/**
 * Heurística opcional para detectar mensajes de error enviados por el backend
 * dentro del campo `message`.
 *
 * ### Uso recomendado
 * - Úsala solo si el backend no tiene códigos de error claros.
 * - Si genera falsos positivos, elimina esta validación y confía
 *   únicamente en la presencia de un ID válido.
 *
 * @return `true` si el mensaje parece indicar un error de autenticación.
 */
private fun String?.isAuthErrorMessage(): Boolean {
    val m = this?.trim()?.lowercase().orEmpty()
    if (m.isBlank()) return false

    return listOf(
        "error", "invalid", "incorrect", "unauthorized", "forbidden", "fail",
        "wrong", "not found", "doesn't exist", "does not exist"
    ).any { it in m }
}