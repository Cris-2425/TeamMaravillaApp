package com.example.teammaravillaapp.data.local.prefs.user

import android.content.Context
import android.net.Uri
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.teammaravillaapp.data.local.prefs.datastore.userPrefsDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Gestión de URIs de recibos asociados a listas de compras.
 *
 * Cada lista puede tener un recibo guardado como URI de imagen, persistido en DataStore.
 *
 * Funcionalidades:
 * - Guardar un recibo para una lista específica.
 * - Limpiar el recibo de una lista.
 *
 * Uso típico:
 * ```
 * @Inject lateinit var receiptsPrefs: ReceiptsPrefs
 *
 * receiptsPrefs.saveReceiptUri(listId, uri)
 * receiptsPrefs.clearReceiptUri(listId)
 * ```
 */
@Singleton
class ReceiptsPrefs @Inject constructor(
    @ApplicationContext private val context: Context
) {

    /** Genera la clave única en DataStore para cada lista */
    private fun keyFor(listId: String) = stringPreferencesKey("receipt_uri_$listId")

    /**
     * Guarda la URI del recibo para la lista indicada.
     *
     * @param listId ID de la lista a la que pertenece el recibo.
     * @param uri URI del recibo (imagen PDF, foto, etc.).
     */
    suspend fun saveReceiptUri(listId: String, uri: Uri) {
        context.userPrefsDataStore.edit { prefs ->
            prefs[keyFor(listId)] = uri.toString()
        }
    }

    /**
     * Elimina el recibo asociado a la lista indicada.
     *
     * @param listId ID de la lista cuyo recibo se eliminará.
     */
    suspend fun clearReceiptUri(listId: String) {
        context.userPrefsDataStore.edit { prefs ->
            prefs.remove(keyFor(listId))
        }
    }
}