package com.example.teammaravillaapp.data.local.prefs.listdetail

import com.example.teammaravillaapp.data.local.prefs.user.CategoryFilterPrefs
import com.example.teammaravillaapp.data.local.prefs.user.ListViewTypePrefs
import com.example.teammaravillaapp.model.ListViewType
import com.example.teammaravillaapp.model.ProductCategory
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Implementación de [ListDetailPrefs] usando DataStore (Preferences).
 *
 * Funciona como un repositorio de configuración de listas de usuario.
 *
 * Delegaciones:
 * - [CategoryFilterPrefs]: persistencia y observación del filtro de categorías.
 * - [ListViewTypePrefs]: persistencia y observación del tipo de vista de listas.
 *
 * Se marca como [Singleton] para garantizar una única instancia en toda la app.
 *
 * Ejemplo de inyección:
 * {@code
 * @Inject lateinit var prefs: ListDetailPrefs
 *
 * val viewTypeFlow = prefs.observeViewType()
 * val selectedCategoriesFlow = prefs.observeSelectedCategories()
 * }
 *
 * @see ListDetailPrefs Interfaz que define el contrato de preferencias para la pantalla de detalle de listas.
 */
@Singleton
class DataStoreListDetailPrefs @Inject constructor(
    private val categoryFilterPrefs: CategoryFilterPrefs,
    private val listViewTypePrefs: ListViewTypePrefs
) : ListDetailPrefs {

    /**
     * Observa las categorías seleccionadas desde DataStore.
     *
     * @return [Flow] de conjunto de [ProductCategory] seleccionadas.
     */
    override fun observeSelectedCategories(): Flow<Set<ProductCategory>> =
        categoryFilterPrefs.observeSelected()

    /**
     * Observa el tipo de vista de la lista (Grid/List) desde DataStore.
     *
     * @return [Flow] de [ListViewType].
     */
    override fun observeViewType(): Flow<ListViewType> =
        listViewTypePrefs.observe()

    /**
     * Limpia el filtro de categorías persistido en DataStore.
     */
    override suspend fun clearCategoryFilter() {
        categoryFilterPrefs.clear()
    }
}