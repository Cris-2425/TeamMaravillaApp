package com.example.teammaravillaapp.data.local.prefs.user

import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Gestión de listas recientes visitadas por el usuario.
 *
 * Funcionalidades:
 * - Observar IDs de listas recientes.
 * - Agregar una lista al historial (con límite máximo).
 * - Eliminar una lista del historial.
 * - Limpiar el historial completo.
 *
 * Implementación delegada a [RecentListsPrefsStore], que encapsula la lógica de persistencia.
 *
 * Uso típico:
 * ```
 * @Inject lateinit var recentListsPrefs: RecentListsPrefs
 *
 * val recentIdsFlow: Flow<List<String>> = recentListsPrefs.observeIds()
 * recentListsPrefs.push(listId)
 * recentListsPrefs.remove(listId)
 * recentListsPrefs.clear()
 * ```
 */
@Singleton
class RecentListsPrefs @Inject constructor(
    private val store: RecentListsPrefsStore
) {

    /** Flujo que emite la lista de IDs recientes en orden cronológico (más reciente primero) */
    fun observeIds(): Flow<List<String>> = store.observeIds()

    /** Agrega una lista al historial reciente, eliminando duplicados y respetando un máximo */
    suspend fun push(listId: String, max: Int = 20) = store.push(listId, max)

    /** Elimina una lista del historial reciente */
    suspend fun remove(listId: String) = store.remove(listId)

    /** Limpia completamente el historial de listas recientes */
    suspend fun clear() = store.clear()
}