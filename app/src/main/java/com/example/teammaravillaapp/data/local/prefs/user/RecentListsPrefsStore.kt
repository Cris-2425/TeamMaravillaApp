package com.example.teammaravillaapp.data.local.prefs.user

import android.content.Context
import androidx.datastore.preferences.core.edit
import com.example.teammaravillaapp.data.local.prefs.datastore.userPrefsDataStore
import com.example.teammaravillaapp.data.local.prefs.keys.PrefKeys
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import org.json.JSONArray
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Capa de persistencia para las listas recientes visitadas por el usuario.
 *
 * Encapsula la lógica de almacenamiento en DataStore, incluyendo:
 * - Observación de IDs recientes mediante Flow.
 * - Inserción de nuevas listas respetando duplicados y límite máximo.
 * - Eliminación de listas individuales.
 * - Limpieza completa del historial.
 *
 * Internamente guarda los IDs como JSON array string bajo la clave
 * [PrefKeys.KEY_RECENT_LIST_IDS].
 *
 * Uso típico:
 * ```
 * @Inject lateinit var store: RecentListsPrefsStore
 *
 * store.observeIds()      // Flow<List<String>>
 * store.push(listId)
 * store.remove(listId)
 * store.clear()
 * ```
 */
@Singleton
class RecentListsPrefsStore @Inject constructor(
    @ApplicationContext private val context: Context
) {

    /** Flujo de IDs recientes, emitido en orden cronológico (más reciente primero) */
    fun observeIds(): Flow<List<String>> =
        context.userPrefsDataStore.data
            .map { prefs ->
                val raw = prefs[PrefKeys.KEY_RECENT_LIST_IDS].orEmpty()
                parseJsonArray(raw)
            }
            .distinctUntilChanged()

    /**
     * Inserta un ID al historial reciente.
     *
     * @param listId ID de la lista.
     * @param max Cantidad máxima de elementos a mantener (por defecto 20).
     */
    suspend fun push(listId: String, max: Int = 20) {
        context.userPrefsDataStore.edit { prefs ->
            val current = parseJsonArray(prefs[PrefKeys.KEY_RECENT_LIST_IDS].orEmpty())
                .filter { it != listId }
                .toMutableList()

            current.add(0, listId)
            prefs[PrefKeys.KEY_RECENT_LIST_IDS] = toJsonArray(current.take(max))
        }
    }

    /** Elimina un ID específico del historial reciente */
    suspend fun remove(listId: String) {
        context.userPrefsDataStore.edit { prefs ->
            val current = parseJsonArray(prefs[PrefKeys.KEY_RECENT_LIST_IDS].orEmpty())
                .filter { it != listId }
            prefs[PrefKeys.KEY_RECENT_LIST_IDS] = toJsonArray(current)
        }
    }

    /** Limpia completamente el historial de listas recientes */
    suspend fun clear() {
        context.userPrefsDataStore.edit { prefs ->
            prefs[PrefKeys.KEY_RECENT_LIST_IDS] = "[]"
        }
    }

    /** Convierte un string JSON a lista de IDs, ignorando elementos vacíos */
    private fun parseJsonArray(raw: String): List<String> {
        if (raw.isBlank()) return emptyList()
        return runCatching {
            val arr = JSONArray(raw)
            buildList(arr.length()) {
                for (i in 0 until arr.length()) add(arr.getString(i))
            }.filter { it.isNotBlank() }
        }.getOrElse { emptyList() }
    }

    /** Convierte una lista de IDs a JSON array string */
    private fun toJsonArray(list: List<String>): String =
        JSONArray(list).toString()
}