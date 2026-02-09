package com.example.teammaravillaapp.page.categoryfilter

import com.example.teammaravillaapp.model.ProductCategory

/**
 * Estado de UI de la pantalla **CategoryFilter**.
 *
 * Encapsula:
 * - Carga inicial desde preferencias.
 * - Conjunto de categorías seleccionadas actualmente.
 *
 * @property isLoading Indica si la pantalla está esperando a cargar el estado persistido.
 * @property selected Conjunto de categorías seleccionadas por el usuario.
 *
 * @see ProductCategory
 */
data class CategoryFilterUiState(
    val isLoading: Boolean = true,
    val selected: Set<ProductCategory> = emptySet()
) {
    /**
     * Conjunto completo de categorías disponibles en la app.
     *
     * @return Set con todas las entradas del enum.
     */
    val all: Set<ProductCategory> = ProductCategory.entries.toSet()

    /**
     * Indica si el usuario tiene seleccionadas **todas** las categorías.
     *
     * @return true si selected contiene todas las categorías; false en caso contrario.
     */
    val allSelected: Boolean get() = selected.size == all.size && all.isNotEmpty()
}