package com.example.teammaravillaapp.data.local.prefs.user

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.teammaravillaapp.data.local.prefs.keys.PrefKeys
import kotlinx.coroutines.flow.first
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Persistencia en **DataStore Preferences** del `fileId` asociado al fichero remoto de favoritos
 * de un usuario concreto.
 *
 * ### Por qué DataStore (y no Room)
 * Este valor es **configuración por usuario** (clave → valor) y no un dato relacional. Mantenerlo
 * en Preferences evita migraciones de esquema y reduce complejidad.
 *
 * ### Clave dinámica
 * Se genera con el prefijo [PrefKeys.KEY_FAVORITES_FILE_ID_PREFIX] para aislar usuarios en el mismo
 * dispositivo (multi-sesión / cambio de cuenta).
 *
 * ## Concurrencia
 * - `DataStore` es **thread-safe**.
 * - Las operaciones son `suspend` y deben invocarse desde coroutines.
 *
 * @property dataStore Instancia de `DataStore<Preferences>` inyectada (Hilt).
 */
@Singleton
class FavoritesFileIdPrefs @Inject constructor(
    private val dataStore: DataStore<Preferences>
) {

    /**
     * Construye la clave de preferencias para el `userId` dado.
     *
     * Se usa `stringPreferencesKey(...)` para mantener el valor como `String` y permitir borrado directo.
     *
     * @param userId Identificador estable del usuario.
     * @return Clave de preferencias específica del usuario.
     */
    private fun key(userId: String) =
        stringPreferencesKey(PrefKeys.KEY_FAVORITES_FILE_ID_PREFIX + userId)

    /**
     * Recupera el `fileId` persistido para el usuario.
     *
     * Esta operación toma el *primer snapshot* del flujo `dataStore.data`.
     *
     * @param userId Identificador estable del usuario.
     * @return `fileId` si existe; `null` en caso contrario.
     */
    suspend fun get(userId: String): String? =
        dataStore.data.first()[key(userId)]

    /**
     * Guarda o actualiza el `fileId` asociado al usuario.
     *
     * @param userId Identificador estable del usuario.
     * @param fileId Identificador del fichero remoto a persistir.
     */
    suspend fun set(userId: String, fileId: String) {
        dataStore.edit { prefs -> prefs[key(userId)] = fileId }
    }

    /**
     * Elimina el `fileId` persistido del usuario.
     *
     * Recomendado en *logout* o cuando el fichero remoto se invalida.
     *
     * @param userId Identificador estable del usuario.
     */
    suspend fun clear(userId: String) {
        dataStore.edit { prefs -> prefs.remove(key(userId)) }
    }
}