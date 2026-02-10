package com.example.teammaravillaapp.data.local.entity

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

/**
 * Representa una receta con todos sus productos asociados.
 *
 * Esta data class permite recuperar la relación muchos-a-muchos entre
 * [RecipeEntity] y [ProductEntity] usando la tabla puente [RecipeIngredientsCrossRef].
 *
 * @property recipe La entidad principal de la receta.
 * @property products Lista de productos asociados a la receta.
 *
 * Ejemplo de uso con DAO:
 * ```
 * @Transaction
 * @Query("SELECT * FROM recipes WHERE id = :recipeId")
 * suspend fun getRecipeWithProducts(recipeId: Int): RecipeWithProductsRoom
 * ```
 */
data class RecipeWithProductsRoom(
    @Embedded val recipe: RecipeEntity,

    @Relation(
        parentColumn = "id",
        entityColumn = "id",
        associateBy = Junction(
            value = RecipeIngredientsCrossRef::class,
            parentColumn = "recipeId",
            entityColumn = "productId"
        )
    )
    val products: List<ProductEntity>
)