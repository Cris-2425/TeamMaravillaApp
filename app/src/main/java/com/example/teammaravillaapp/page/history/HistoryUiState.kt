package com.example.teammaravillaapp.page.history

/**
 * Estado de UI de la pantalla de historial.
 *
 * Representa el estado mínimo necesario para pintar:
 * - Si la pantalla está cargando.
 * - La lista de filas (id + nombre).
 *
 * @property isLoading Indica si la pantalla aún está resolviendo datos.
 * Restricciones:
 * - true muestra el estado de carga aunque haya filas.
 * @property rows Filas del historial ordenadas (normalmente) por orden de “más reciente”.
 * Restricciones:
 * - Puede ser lista vacía.
 *
 * Ejemplo:
 * {@code
 * val state = HistoryUiState(
 *   isLoading = false,
 *   rows = listOf(HistoryRow("id1", "Compra semanal"))
 * )
 * }
 */
data class HistoryUiState(
    val isLoading: Boolean = true,
    val rows: List<HistoryRow> = emptyList()
)

/**
 * Modelo de fila del historial.
 *
 * @property id Identificador estable de la lista (clave).
 * Restricciones:
 * - No debería estar vacío.
 * @property name Nombre visible de la lista para mostrar en UI.
 * Restricciones:
 * - No nulo, puede ser vacío pero no recomendable.
 *
 * Ejemplo:
 * {@code
 * HistoryRow(id = "l1", name = "BBQ sábado")
 * }
 */
data class HistoryRow(
    val id: String,
    val name: String
)