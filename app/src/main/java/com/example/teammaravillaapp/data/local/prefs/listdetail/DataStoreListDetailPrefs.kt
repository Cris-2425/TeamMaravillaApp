package com.example.teammaravillaapp.data.local.prefs.listdetail

import com.example.teammaravillaapp.data.local.prefs.user.CategoryFilterPrefs
import com.example.teammaravillaapp.data.local.prefs.user.ListViewTypePrefs
import com.example.teammaravillaapp.model.ListViewType
import com.example.teammaravillaapp.model.ProductCategory
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Implementación de [ListDetailPrefs] basada en DataStore (Preferences).
 *
 * Delega en:
 * - [CategoryFilterPrefs] para persistir/observar el filtro de categorías.
 * - [ListViewTypePrefs] para persistir/observar el tipo de vista.
 *
 * Se marca como [Singleton] para reutilizar la misma instancia y evitar lecturas duplicadas.
 *
 * Ejemplo de uso:
 * {@code
 * @Inject lateinit var prefs: ListDetailPrefs
 *
 * val viewType = prefs.observeViewType()
 * val selectedCategories = prefs.observeSelectedCategories()
 * }
 *
 * @see ListDetailPrefs
 */
@Singleton
class DataStoreListDetailPrefs @Inject constructor(
    private val categoryFilterPrefs: CategoryFilterPrefs,
    private val listViewTypePrefs: ListViewTypePrefs
) : ListDetailPrefs {

    /**
     * Observa categorías seleccionadas desde DataStore.
     */
    override fun observeSelectedCategories(): Flow<Set<ProductCategory>> =
        categoryFilterPrefs.observeSelected()

    /**
     * Observa el tipo de vista desde DataStore.
     */
    override fun observeViewType(): Flow<ListViewType> =
        listViewTypePrefs.observe()

    /**
     * Limpia el filtro de categorías persistido.
     */
    override suspend fun clearCategoryFilter() {
        categoryFilterPrefs.clear()
    }
}