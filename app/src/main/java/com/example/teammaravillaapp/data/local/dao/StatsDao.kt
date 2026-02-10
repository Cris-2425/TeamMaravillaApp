package com.example.teammaravillaapp.data.local.dao

import androidx.room.Dao
import androidx.room.Query

/**
 * DAO para estadísticas y métricas de la base de datos local.
 *
 * Proporciona conteos de listas, productos, recetas, favoritos y items,
 * además de consultas específicas como top productos y recientes.
 */
@Dao
interface StatsDao {
    @Query("SELECT COUNT(*) FROM user_lists")
    suspend fun countLists(): Int

    @Query("SELECT COUNT(*) FROM products")
    suspend fun countProducts(): Int

    @Query("SELECT COUNT(*) FROM recipes")
    suspend fun countRecipes(): Int

    @Query("SELECT COUNT(*) FROM favorite_recipes")
    suspend fun countFavorites(): Int

    // --- Items ---
    @Query("SELECT COUNT(*) FROM list_items")
    suspend fun countItems(): Int

    @Query("SELECT COUNT(*) FROM list_items WHERE checked = 1")
    suspend fun countCheckedItems(): Int

    @Query("""
        SELECT COUNT(*) FROM user_lists
        WHERE createdAt >= :from
    """)
    suspend fun countListsSince(from: Long): Int

    @Query("""
        SELECT COUNT(*) FROM list_items
        WHERE addedAt >= :from
    """)
    suspend fun countItemsSince(from: Long): Int

    @Query("""
        SELECT p.name AS name, COUNT(li.productId) AS times
        FROM list_items li
        JOIN products p ON p.id = li.productId
        GROUP BY li.productId
        ORDER BY times DESC
        LIMIT 5
    """)
    suspend fun topProducts(): List<TopProductRow>
}

/**
 * DTO para representar un producto más utilizado en las listas.
 *
 * @property name Nombre del producto.
 * @property times Veces que el producto aparece en listas.
 */
data class TopProductRow(
    val name: String,
    val times: Int
)