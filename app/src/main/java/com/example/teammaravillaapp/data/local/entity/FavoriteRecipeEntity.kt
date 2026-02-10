package com.example.teammaravillaapp.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Representa una receta marcada como favorita por el usuario.
 *
 * Esta entidad almacena únicamente el ID de la receta.
 *
 * Tabla: favorite_recipes
 *
 * @property recipeId ID de la receta favorita.
 */
@Entity(tableName = "favorite_recipes")
data class FavoriteRecipeEntity(
    @PrimaryKey val recipeId: Int
)