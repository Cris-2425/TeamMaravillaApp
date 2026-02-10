package com.example.teammaravillaapp.page.stats

/**
 * Estado de UI para la pantalla de estadísticas.
 *
 * Contiene un snapshot agregado (conteos y rankings) calculado por [StatsRepository].
 *
 * @property isLoading Indica si la pantalla está cargando datos.
 * Restricciones:
 * - Si es true, se recomienda que [error] sea null.
 * @property error Error técnico de carga (I/O, DB, parsing, etc.).
 * Restricciones:
 * - Debe ser null cuando no haya error.
 * @property lists Número total de listas disponibles.
 * @property products Número total de productos en catálogo.
 * @property recipes Número total de recetas disponibles.
 * @property favorites Número total de recetas marcadas como favoritas.
 * @property totalItems Total de ítems agregados a listas (suma de items).
 * @property checkedItems Total de ítems marcados como completados.
 * @property listsLast7Days Listas creadas en los últimos 7 días.
 * @property itemsLast7Days Ítems añadidos en los últimos 7 días.
 * @property topProducts Ranking de productos más utilizados.
 *
 * Ejemplo de uso:
 * {@code
 * val state = StatsUiState(
 *   isLoading = false,
 *   lists = 12,
 *   products = 120,
 *   topProducts = listOf(TopProductStat("Leche", 8))
 * )
 * }
 */
data class StatsUiState(
    val isLoading: Boolean = false,
    val error: Throwable? = null,
    val lists: Int = 0,
    val products: Int = 0,
    val recipes: Int = 0,
    val favorites: Int = 0,
    val totalItems: Int = 0,
    val checkedItems: Int = 0,
    val listsLast7Days: Int = 0,
    val itemsLast7Days: Int = 0,
    val topProducts: List<TopProductStat> = emptyList()
)

/**
 * Entrada del ranking de productos más utilizados.
 *
 * @property name Nombre visible del producto.
 * Restricciones:
 * - No debería ser blank.
 * @property times Número de veces que aparece/ha sido usado.
 * Restricciones:
 * - Debe ser >= 0.
 */
data class TopProductStat(
    val name: String,
    val times: Int
)