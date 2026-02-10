package com.example.teammaravillaapp.data.local.repository.stats

import com.example.teammaravillaapp.page.stats.TopProductStat

/**
 * Snapshot de estadísticas de la aplicación.
 *
 * Contiene métricas generales, métricas de los últimos 7 días y los productos más utilizados.
 *
 * @property lists Número total de listas creadas.
 * @property products Número total de productos.
 * @property recipes Número total de recetas.
 * @property favorites Número total de recetas marcadas como favoritas.
 * @property totalItems Número total de items en todas las listas.
 * @property checkedItems Número de items marcados como completados.
 * @property listsLast7Days Número de listas creadas en los últimos 7 días.
 * @property itemsLast7Days Número de items añadidos en los últimos 7 días.
 * @property topProducts Lista de productos más frecuentes, ordenados por uso.
 */
data class StatsSnapshot(
    val lists: Int,
    val products: Int,
    val recipes: Int,
    val favorites: Int,
    val totalItems: Int,
    val checkedItems: Int,
    val listsLast7Days: Int,
    val itemsLast7Days: Int,
    val topProducts: List<TopProductStat>
)