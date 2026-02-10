package com.example.teammaravillaapp.data.session

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

/**
 * DataStore de sesión (Preferences) para persistir y observar el estado de autenticación.
 *
 * Motivo:
 * - Centralizar el “source of truth” de la sesión (loggedIn, username, token, rememberMe).
 * - Exponer flows para que la UI/ViewModels reaccionen automáticamente a cambios.
 * - Separar persistencia (DataStore) de la lógica de negocio (repositorios/VM).
 *
 * Decisiones de diseño:
 * - `rememberMe` por defecto es `true` (comportamiento cómodo por defecto).
 * - `logged_in` por defecto es `false` (sesión no iniciada).
 * - `username` y `token` pueden ser null si no existen en preferencias.
 *
 * @param appContext Contexto de aplicación (no Activity) requerido por DataStore.
 * Restricciones:
 * - Debe ser el contexto con ciclo de vida de aplicación.
 * - No nulo.
 *
 * @throws Exception No se lanza explícitamente desde la API pública.
 * Fallos técnicos posibles (no tipados):
 * - IOException / corrupción de DataStore durante lectura/escritura puede producir excepciones.
 * Recomendación: envolver `dataStore.data` con `catch { emit(emptyPreferences()) }` si quieres
 * evitar crashes y degradar a valores por defecto.
 *
 * @see com.example.teammaravillaapp.page.session.SessionViewModel Consumidor típico de estos flows.
 *
 * Ejemplo de uso:
 * {@code
 * // Login OK
 * sessionStore.saveSession(username = "cris", token = "abc", rememberMe = true)
 *
 * // UI: observar estado
 * sessionStore.isLoggedIn.map { logged -> ... }
 *
 * // Logout
 * sessionStore.clearSession()
 * }
 */
@Singleton
class SessionStore @Inject constructor(
    @ApplicationContext private val appContext: Context
) {

    /**
     * Claves internas de DataStore.
     *
     * Se mantienen encapsuladas para:
     * - evitar colisiones de nombres
     * - permitir refactors sin afectar consumidores
     */
    private object Keys {
        /** Flag de sesión iniciada. */
        val LOGGED_IN: Preferences.Key<Boolean> = booleanPreferencesKey("logged_in")

        /** Username visible (puede no existir). */
        val USERNAME: Preferences.Key<String> = stringPreferencesKey("username")

        /** Token/credencial (si aplica). Puede no existir. */
        val TOKEN: Preferences.Key<String> = stringPreferencesKey("token")

        /** Preferencia “recordarme”. */
        val REMEMBER_ME: Preferences.Key<Boolean> = booleanPreferencesKey("remember_me")
    }

    /**
     * Preferencia “recordarme”.
     *
     * Semántica recomendada:
     * - Controla persistencia entre reinicios, no necesariamente si estás logueado “ahora”.
     *
     * Valor por defecto: `true` si no existe la clave.
     *
     * @return Flow<Boolean> que emite el valor actual de “remember me”.
     *
     * @throws Exception No se lanza explícitamente.
     * DataStore podría lanzar IOException en lectura si no se gestiona con `catch`.
     */
    val rememberMe: Flow<Boolean> =
        appContext.dataStore.data.map { it[Keys.REMEMBER_ME] ?: true }

    /**
     * Indica si hay una sesión marcada como activa.
     *
     * Valor por defecto: `false` si no existe la clave.
     *
     * @return Flow<Boolean> que emite el estado de `logged_in`.
     *
     * @throws Exception No se lanza explícitamente.
     * DataStore podría lanzar IOException en lectura si no se gestiona con `catch`.
     */
    val isLoggedIn: Flow<Boolean> =
        appContext.dataStore.data.map { prefs -> prefs[Keys.LOGGED_IN] ?: false }

    /**
     * Username persistido.
     *
     * @return Flow<String?> que emite el username o null si no existe.
     *
     * @throws Exception No se lanza explícitamente.
     * DataStore podría lanzar IOException en lectura si no se gestiona con `catch`.
     */
    val username: Flow<String?> =
        appContext.dataStore.data.map { prefs -> prefs[Keys.USERNAME] }

    /**
     * Token persistido.
     *
     * @return Flow<String?> que emite el token o null si no existe.
     *
     * @throws Exception No se lanza explícitamente.
     * DataStore podría lanzar IOException en lectura si no se gestiona con `catch`.
     */
    val token: Flow<String?> =
        appContext.dataStore.data.map { prefs -> prefs[Keys.TOKEN] }

    /**
     * Guarda o actualiza la sesión tras un login exitoso.
     *
     * Responsabilidad:
     * - Marcar la sesión como activa (`logged_in = true`).
     * - Persistir username.
     * - Persistir rememberMe (para decidir auto-login en arranque).
     * - Persistir/eliminar token según corresponda.
     *
     * Nota:
     * - `rememberMe` NO decide si el usuario está logueado ahora.
     *   Solo decide si la sesión debe restaurarse automáticamente
     *   tras reiniciar la aplicación.
     *
     * @param username Nombre de usuario persistido.
     * @param token Token opcional (si tu backend lo usa).
     * @param rememberMe Indica si se permite auto-login en reinicios.
     *
     * @throws IOException / IllegalStateException si DataStore falla al escribir.
     */
    suspend fun saveSession(username: String, token: String? = null, rememberMe: Boolean) {
        appContext.dataStore.edit { prefs ->
            prefs[Keys.LOGGED_IN] = true
            prefs[Keys.USERNAME] = username
            prefs[Keys.REMEMBER_ME] = rememberMe
            if (token != null) prefs[Keys.TOKEN] = token else prefs.remove(Keys.TOKEN)
        }
    }

    /**
     * Limpia completamente la sesión (logout).
     *
     * Responsabilidades:
     * - Marcar `logged_in = false`.
     * - Eliminar username.
     * - Eliminar token.
     *
     * Importante:
     * - NO modifica el valor de `remember_me`.
     *   Se respeta la última elección del usuario.
     *
     * @throws IOException / IllegalStateException si DataStore falla.
     */
    suspend fun clearSession() {
        appContext.dataStore.edit { prefs ->
            prefs[Keys.LOGGED_IN] = false
            prefs.remove(Keys.USERNAME)
            prefs.remove(Keys.TOKEN)
        }
    }

    /**
     * Devuelve el token actual (si existe) como lectura inmediata.
     *
     * Motivo:
     * - Facilitar consumo desde interceptores/clients (Retrofit/OkHttp) o repositorios.
     *
     * @return token persistido o null si no existe.
     *
     * @throws Exception Técnicas:
     * - Excepciones de lectura DataStore al usar `first()`.
     *
     * Ejemplo de uso:
     * {@code
     * val token = sessionStore.getTokenOrNull()
     * if (token != null) { ... }
     * }
     */
    suspend fun getTokenOrNull(): String? = token.first()
}

/**
 * Extensión interna de DataStore.
 *
 * @see SessionStore Consumidor principal.
 */
private val Context.dataStore by preferencesDataStore(name = "session_prefs")