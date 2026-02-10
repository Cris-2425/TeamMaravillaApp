package com.example.teammaravillaapp.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Representa una receta almacenada en la base de datos local.
 *
 * Esta entidad es utilizada para persistir información de recetas, incluyendo
 * título, instrucciones y una imagen asociada.
 *
 * @property id Identificador único de la receta.
 * @property title Nombre o título de la receta.
 * @property imageRes Recurso drawable de la imagen asociada a la receta (opcional).
 * @property instructions Instrucciones completas para preparar la receta.
 *
 * Ejemplo de uso con DAO:
 * ```
 * @Query("SELECT * FROM recipes WHERE id = :recipeId")
 * suspend fun getRecipeById(recipeId: Int): RecipeEntity?
 * ```
 */
@Entity(tableName = "recipes")
data class RecipeEntity(
    @PrimaryKey val id: Int,
    val title: String,
    val imageRes: Int?,
    val instructions: String
)