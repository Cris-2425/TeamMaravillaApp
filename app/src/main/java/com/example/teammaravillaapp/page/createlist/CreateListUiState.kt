package com.example.teammaravillaapp.page.createlist

import androidx.annotation.StringRes
import com.example.teammaravillaapp.model.ListBackground
import com.example.teammaravillaapp.model.Product

/**
 * Estado de UI para la pantalla de **creación de listas**.
 *
 * Objetivo:
 * - Representar todos los datos que la UI necesita pintar (nombre, fondo, catálogo, selección).
 * - Evitar lógica de UI dispersa en Compose (la pantalla solo renderiza este estado).
 *
 * @property name Nombre introducido por el usuario. Puede contener espacios; usar [trimmedName] para validación.
 * @property selectedBackground Fondo seleccionado para la lista. Nunca nulo.
 * @property isLoadingCatalog Indica si el catálogo de productos se está cargando.
 * @property catalogErrorResId Recurso de string opcional para mostrar un error (i18n).
 * @property catalogProducts Catálogo de productos disponible (ej. desde Room/cache).
 * @property selectedProducts Productos que se añadirán al crear la lista (por ejemplo desde sugeridas).
 *
 * @see CreateListViewModel
 * @see com.example.teammaravillaapp.component.BackgroundGrid
 */
data class CreateListUiState(
    val name: String = "",
    val selectedBackground: ListBackground = ListBackground.FONDO1,

    val isLoadingCatalog: Boolean = false,
    @StringRes val catalogErrorResId: Int? = null,

    val catalogProducts: List<Product> = emptyList(),

    // productos que tendrá la lista al crearla (vía sugeridas)
    val selectedProducts: List<Product> = emptyList()
) {
    /**
     * Nombre normalizado (sin espacios al inicio/fin) para validación y guardado.
     *
     * @return Nombre “trimmed”.
     */
    val trimmedName: String get() = name.trim()
}