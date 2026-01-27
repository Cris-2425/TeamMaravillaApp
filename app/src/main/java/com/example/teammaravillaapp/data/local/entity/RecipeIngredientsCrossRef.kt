package com.example.teammaravillaapp.data.local.entity

import androidx.room.Entity
import androidx.room.Index

@Entity(
    tableName = "recipe_ingredients",
    primaryKeys = ["recipeId", "productId"],
    indices = [Index("recipeId"), Index("productId")]
)
data class RecipeIngredientsCrossRef(
    val recipeId: Int,
    val productId: String,
    val quantity: Double? = null,   // 2.0, 200.0...
    val unit: String? = null,       // "uds", "g", "ml"
    val position: Int = 0           // orden en la receta
)
