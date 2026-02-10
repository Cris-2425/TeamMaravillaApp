package com.example.teammaravillaapp.data.local.entity

import androidx.room.Entity
import androidx.room.Index

/**
 * Representa la relación muchos-a-muchos entre [RecipeEntity] y [ProductEntity].
 *
 * Esta entidad sirve como tabla puente (cross-ref) para vincular productos con recetas
 * incluyendo información adicional como cantidad, unidad y posición en la receta.
 *
 * @property recipeId ID de la receta asociada.
 * @property productId ID del producto asociado.
 * @property quantity Cantidad del producto en la receta (opcional).
 * @property unit Unidad de medida del producto (opcional, por ejemplo "g", "ml", "uds").
 * @property position Posición del ingrediente en la lista de la receta (permite ordenamiento).
 *
 * Ejemplo de uso con DAO:
 * ```
 * @Insert(onConflict = OnConflictStrategy.REPLACE)
 * suspend fun insertRecipeIngredient(crossRef: RecipeIngredientsCrossRef)
 * ```
 */
@Entity(
    tableName = "recipe_ingredients",
    primaryKeys = ["recipeId", "productId"],
    indices = [Index("recipeId"), Index("productId")]
)
data class RecipeIngredientsCrossRef(
    val recipeId: Int,
    val productId: String,
    val quantity: Double? = null,
    val unit: String? = null,
    val position: Int = 0
)