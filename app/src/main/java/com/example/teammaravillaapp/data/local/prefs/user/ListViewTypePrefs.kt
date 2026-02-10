package com.example.teammaravillaapp.data.local.prefs.user

import android.content.Context
import androidx.datastore.preferences.core.edit
import com.example.teammaravillaapp.data.local.prefs.datastore.userPrefsDataStore
import com.example.teammaravillaapp.data.local.prefs.keys.PrefKeys
import com.example.teammaravillaapp.model.ListViewType
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Capa de acceso a DataStore para persistir y observar el tipo de vista de listas.
 *
 * Responsable de:
 * - Exponer un flujo de [ListViewType] listo para UI.
 * - Guardar el tipo de vista seleccionado por el usuario.
 * - Manejar valores por defecto y fallback seguro si el valor guardado no es válido.
 *
 * Uso típico desde ViewModel:
 * ```
 * val viewTypeFlow: Flow<ListViewType> = listViewTypePrefs.observe()
 * ```
 */
@Singleton
class ListViewTypePrefs @Inject constructor(
    @ApplicationContext private val context: Context
) {

    /**
     * Flujo que emite el tipo de vista actual de las listas.
     * Valor por defecto: [ListViewType.BUBBLES].
     */
    fun observe(): Flow<ListViewType> =
        context.userPrefsDataStore.data
            .map { prefs ->
                val raw = prefs[PrefKeys.KEY_LIST_VIEW_TYPE]
                runCatching { raw?.let { ListViewType.valueOf(it) } }.getOrNull()
                    ?: ListViewType.BUBBLES
            }
            .distinctUntilChanged()

    /**
     * Persiste el tipo de vista seleccionado.
     *
     * @param value Tipo de vista a guardar.
     */
    suspend fun set(value: ListViewType) {
        context.userPrefsDataStore.edit { prefs ->
            prefs[PrefKeys.KEY_LIST_VIEW_TYPE] = value.name
        }
    }
}