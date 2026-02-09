package com.example.teammaravillaapp.data.local.prefs.listdetail

import com.example.teammaravillaapp.model.ListViewType
import com.example.teammaravillaapp.model.ProductCategory
import kotlinx.coroutines.flow.Flow

/**
 * Contrato de preferencias (persistencia ligera) específicas para la pantalla ListDetail.
 *
 * Se usa para guardar y observar:
 * - Categorías seleccionadas en el filtro
 * - Tipo de vista (burbujas/lista/compacto)
 *
 * Implementación típica:
 * - DataStore (Preferences) como almacenamiento local.
 *
 * Ejemplo de uso:
 * {@code
 * val viewTypeFlow: Flow<ListViewType> = prefs.observeViewType()
 * val selectedCats: Flow<Set<ProductCategory>> = prefs.observeSelectedCategories()
 * }
 *
 * @see DataStoreListDetailPrefs
 */
interface ListDetailPrefs {

    /**
     * Observa el conjunto de categorías seleccionadas como filtro.
     *
     * @return Un [Flow] que emite el conjunto de categorías seleccionadas. Puede estar vacío.
     */
    fun observeSelectedCategories(): Flow<Set<ProductCategory>>

    /**
     * Observa el tipo de vista preferido para representar los productos.
     *
     * @return Un [Flow] que emite el [ListViewType] actual.
     */
    fun observeViewType(): Flow<ListViewType>

    /**
     * Limpia (resetea) el filtro de categorías.
     *
     * Uso típico: opción del menú “limpiar filtro” o “mostrar todo”.
     */
    suspend fun clearCategoryFilter()
}