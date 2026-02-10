package com.example.teammaravillaapp.data.local.repository.stats

import com.example.teammaravillaapp.data.local.dao.StatsDao
import com.example.teammaravillaapp.page.stats.TopProductStat
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Repositorio encargado de recopilar estadísticas de la aplicación.
 *
 * Combina información de listas, productos, recetas y favoritos usando [StatsDao].
 * Devuelve un snapshot de estadísticas listo para presentar en UI.
 *
 * Ejemplo de uso:
 * ```
 * @Inject lateinit var statsRepository: StatsRepository
 *
 * val snapshot = statsRepository.loadStats()
 * ```
 */
@Singleton
class StatsRepository @Inject constructor(
    private val dao: StatsDao
) {

    /**
     * Carga un snapshot completo de estadísticas.
     *
     * Calcula métricas generales, métricas de los últimos 7 días y top productos.
     *
     * @return [StatsSnapshot] con todos los datos agregados.
     */
    suspend fun loadStats(): StatsSnapshot {
        val now = System.currentTimeMillis()
        val weekAgo = now - 7L * 24 * 60 * 60 * 1000

        return StatsSnapshot(
            lists = dao.countLists(),
            products = dao.countProducts(),
            recipes = dao.countRecipes(),
            favorites = dao.countFavorites(),
            totalItems = dao.countItems(),
            checkedItems = dao.countCheckedItems(),
            listsLast7Days = dao.countListsSince(weekAgo),
            itemsLast7Days = dao.countItemsSince(weekAgo),
            topProducts = dao.topProducts().map {
                TopProductStat(it.name, it.times)
            }
        )
    }
}