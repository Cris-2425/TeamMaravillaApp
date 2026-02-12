package com.example.teammaravillaapp.data.session

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton
import com.example.teammaravillaapp.page.session.SessionViewModel

/**
 * Almacén de sesión basado en **DataStore Preferences**.
 *
 * Centraliza el estado de autenticación como **source of truth** observable:
 * - `logged_in` (sesión activa)
 * - `username`
 * - `token`
 * - `remember_me`
 *
 * ### Por qué DataStore
 * Este estado es **configuración de sesión** (clave → valor), no un modelo relacional.
 * DataStore ofrece persistencia asíncrona y `Flow` nativo para reaccionar a cambios.
 *
 * ### Tolerancia a fallos
 * Los `Flow` de lectura degradan a [emptyPreferences] ante [IOException] para evitar crasheos por
 * corrupción/transitorios de IO, manteniendo valores por defecto consistentes.
 *
 * ## Concurrencia
 * - `DataStore` es **thread-safe**.
 * - Todas las escrituras son `suspend` y deben ejecutarse desde coroutines.
 *
 * @property appContext Contexto de aplicación (no Activity), requerido por DataStore.
 *
 * @see SessionViewModel Consumidor típico de estos flows.
 */
@Singleton
class SessionStore @Inject constructor(
    @ApplicationContext private val appContext: Context
) {

    /**
     * Claves internas de DataStore.
     *
     * Se encapsulan para:
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
     * Semántica: controla si la sesión debe restaurarse tras reiniciar la app, no si la sesión está
     * activa *en este instante*.
     *
     * Valor por defecto: `true`.
     *
     * @return `Flow` que emite el valor actual de “remember me”.
     *
     * @throws IOException Si falla la lectura y no se intercepta (las lecturas aquí lo interceptan).
     */
    val rememberMe: Flow<Boolean> =
        appContext.dataStore.data
            .catch { exception ->
                if (exception is IOException) emit(emptyPreferences())
                else throw exception
            }
            .map { it[Keys.REMEMBER_ME] ?: true }

    /**
     * Indica si hay una sesión marcada como activa.
     *
     * Valor por defecto: `false`.
     *
     * @return `Flow` que emite el estado de `logged_in`.
     *
     * @throws IOException Si falla la lectura y no se intercepta (las lecturas aquí lo interceptan).
     */
    val isLoggedIn: Flow<Boolean> =
        appContext.dataStore.data
            .catch { exception ->
                if (exception is IOException) emit(emptyPreferences())
                else throw exception
            }
            .map { prefs -> prefs[Keys.LOGGED_IN] ?: false }

    /**
     * Username persistido, si existe.
     *
     * @return `Flow` que emite el username o `null` si no está persistido.
     */
    val username: Flow<String?> =
        appContext.dataStore.data
            .catch { exception ->
                if (exception is IOException) emit(emptyPreferences())
                else throw exception
            }
            .map { prefs -> prefs[Keys.USERNAME] }

    /**
     * Token persistido, si existe.
     *
     * Se modela como `String?` para permitir limpiar el token sin afectar el resto de preferencias.
     *
     * @return `Flow` que emite el token o `null` si no está persistido.
     */
    val token: Flow<String?> =
        appContext.dataStore.data
            .catch { exception ->
                if (exception is IOException) emit(emptyPreferences())
                else throw exception
            }
            .map { prefs -> prefs[Keys.TOKEN] }

    /**
     * Persiste el resultado de un login exitoso.
     *
     * Responsabilidad:
     * - Marcar `logged_in = true`
     * - Persistir `username`
     * - Persistir `remember_me`
     * - Persistir o eliminar `token` (según se provea)
     *
     * @param username Nombre de usuario a persistir.
     * @param token Token opcional (si el backend lo usa).
     * @param rememberMe Si `true`, habilita restauración de sesión tras reinicio.
     *
     * @throws IOException Si falla la escritura en DataStore.
     * @throws IllegalStateException Si DataStore no está en un estado válido.
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
     * Limpia la sesión activa (logout) sin alterar la preferencia de “recordarme”.
     *
     * ### Por qué no toca `remember_me`
     * `remember_me` representa una preferencia del usuario y puede mantenerse aunque la sesión
     * actual se cierre (p. ej. el usuario decide “recordarme” para el siguiente login).
     *
     * @throws IOException Si falla la escritura en DataStore.
     * @throws IllegalStateException Si DataStore no está en un estado válido.
     */
    suspend fun clearSession() {
        appContext.dataStore.edit { prefs ->
            prefs[Keys.LOGGED_IN] = false
            prefs.remove(Keys.USERNAME)
            prefs.remove(Keys.TOKEN)
        }
    }

    /**
     * Lectura inmediata del token persistido, si existe.
     *
     * Útil en componentes que requieren acceso puntual (p. ej. repositorios o interceptores),
     * evitando colecciones reactivas cuando no aportan valor.
     *
     * @return Token persistido o `null`.
     *
     * @throws Exception Si ocurre un fallo de lectura en `first()` (p. ej. IO no recuperable).
     */
    suspend fun getTokenOrNull(): String? = token.first()
}

/**
 * `DataStore<Preferences>` asociado al contexto de aplicación para persistir sesión.
 *
 * Se mantiene a nivel de fichero para limitar su visibilidad al módulo y evitar uso accidental
 * desde capas no previstas.
 *
 * @see SessionStore Consumidor principal.
 */
private val Context.dataStore by preferencesDataStore(name = "session_prefs")