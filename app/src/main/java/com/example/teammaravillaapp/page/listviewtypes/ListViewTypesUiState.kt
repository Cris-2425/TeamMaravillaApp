package com.example.teammaravillaapp.page.listviewtypes

import com.example.teammaravillaapp.model.ListViewType

/**
 * Estado de UI para la pantalla de selección de tipo de vista.
 *
 * @property isLoading Indica si la preferencia aún se está cargando desde DataStore.
 * Restricciones:
 * - Mientras sea true, se recomienda deshabilitar acciones de guardado.
 * @property selected Tipo de vista actualmente seleccionado.
 * Restricciones:
 * - No nulo (enum).
 *
 * Ejemplo:
 * {@code
 * val st = ListViewTypesUiState(isLoading = false, selected = ListViewType.LIST)
 * }
 */
data class ListViewTypesUiState(
    val isLoading: Boolean = true,
    val selected: ListViewType = ListViewType.BUBBLES
)