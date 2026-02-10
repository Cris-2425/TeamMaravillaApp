package com.example.teammaravillaapp.data.local.prefs.user

import android.content.Context
import androidx.datastore.preferences.core.edit
import com.example.teammaravillaapp.data.local.prefs.datastore.userPrefsDataStore
import com.example.teammaravillaapp.data.local.prefs.keys.PrefKeys
import com.example.teammaravillaapp.model.ProductCategory
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Capa de acceso a DataStore para persistir y observar el filtro de categorías.
 *
 * - Observa cambios usando Flow.
 * - Guarda sets completos o los limpia según se necesite.
 * - Maneja parsing seguro de enums [ProductCategory] con fallback.
 *
 * Esta clase es usada internamente por [DataStoreListDetailPrefs] y [UserPrefsRepositoryImpl].
 */
@Singleton
class CategoryFilterPrefs @Inject constructor(
    @ApplicationContext private val context: Context
) {

    /** Flujo de categorías seleccionadas (visibles). */
    fun observeSelected(): Flow<Set<ProductCategory>> =
        context.userPrefsDataStore.data
            .map { prefs ->
                val raw = prefs[PrefKeys.KEY_CATEGORY_FILTER_SET].orEmpty()
                raw.mapNotNull { name ->
                    runCatching { ProductCategory.valueOf(name) }.getOrNull()
                }.toSet()
            }
            .distinctUntilChanged()

    /** Guarda el set de categorías seleccionadas. */
    suspend fun saveSelected(selected: Set<ProductCategory>) {
        context.userPrefsDataStore.edit { prefs ->
            prefs[PrefKeys.KEY_CATEGORY_FILTER_SET] = selected.map { it.name }.toSet()
        }
    }

    /** Limpia todas las categorías seleccionadas (restablece visibilidad completa). */
    suspend fun clear() {
        context.userPrefsDataStore.edit { prefs ->
            prefs[PrefKeys.KEY_CATEGORY_FILTER_SET] = emptySet()
        }
    }
}