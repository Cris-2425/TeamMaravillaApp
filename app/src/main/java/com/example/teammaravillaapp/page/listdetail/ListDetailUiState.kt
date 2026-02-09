package com.example.teammaravillaapp.page.listdetail

import com.example.teammaravillaapp.model.ListViewType
import com.example.teammaravillaapp.model.Product
import com.example.teammaravillaapp.model.ProductCategory
import com.example.teammaravillaapp.model.UserList

/**
 * Modelo de UI para la pantalla **ListDetail**.
 *
 * La pantalla debe “pintar” únicamente en base a esta estructura, sin calcular lógica de negocio.
 * Este estado se compone habitualmente en un use case (combine de flows) y se expone desde el ViewModel.
 *
 * Campos principales:
 * - Identidad y cabecera de la lista: [listId], [userList]
 * - Catálogo y estados de carga: [isLoadingCatalog], [catalogError]
 * - Preferencias de vista/filtro: [viewType], [selectedCategories]
 * - Búsqueda: [query], [searchResults]
 * - Items actuales: [items]
 * - Sugerencias: [recentAvailable], [availableByCategory]
 *
 * @property listId Id de la lista actual (puede ser null si no hay lista seleccionada).
 * @property userList Datos de la lista (nombre, fondo, etc.) o null si no existe.
 * @property isLoadingCatalog Indica si el catálogo está en “estado inicial / cargando”.
 * @property catalogError Mensaje de error opcional para mostrar en UI (si se usa).
 * @property viewType Tipo de representación del listado (burbujas/lista/compacto).
 * @property selectedCategories Categorías activas en el filtro (vacío = sin filtro).
 * @property query Texto de búsqueda actual.
 * @property searchResults Resultados del catálogo filtrado por [query] y categorías.
 * @property items Productos actualmente en la lista con metadatos (checked/cantidad/posición).
 * @property recentAvailable Sugerencias rápidas (ej. recientes) disponibles para añadir.
 * @property availableByCategory Catálogo disponible agrupado por categoría (excluyendo ya añadidos).
 */
data class ListDetailUiState(
    val listId: String? = null,
    val userList: UserList? = null,

    val isLoadingCatalog: Boolean = true,
    val catalogError: String? = null,

    val viewType: ListViewType = ListViewType.BUBBLES,
    val selectedCategories: Set<ProductCategory> = emptySet(),

    val query: String = "",
    val searchResults: List<Product> = emptyList(),

    val items: List<ListItemUi> = emptyList(),

    val recentAvailable: List<Product> = emptyList(),
    val availableByCategory: Map<ProductCategory, List<Product>> = emptyMap()
) {
    /**
     * Indica estado “vacío” cuando no existe lista cargada.
     * Útil para UI: mostrar error/placeholder de lista no encontrada.
     */
    val isEmptyState: Boolean get() = userList == null

    /** Indica si hay al menos un elemento marcado como checked. */
    val anyChecked: Boolean get() = items.any { it.checked }

    /** Indica si hay contenido para permitir “vaciar lista”. */
    val canClear: Boolean get() = items.isNotEmpty()
}

/**
 * Item de UI dentro de una lista.
 *
 * Contiene el [Product] y los metadatos asociados a la relación “producto en lista”.
 *
 * @property product Producto del catálogo (id, nombre, imagen, categoría…).
 * @property checked Si el usuario lo ha marcado como comprado/completado.
 * @property quantity Cantidad mínima 1.
 * @property position Posición para orden estable (drag & drop o inserción ordenada).
 */
data class ListItemUi(
    val product: Product,
    val checked: Boolean,
    val quantity: Int,
    val position: Int
)