package com.example.teammaravillaapp.data.local.entity

import androidx.room.Entity

@Entity(
    tableName = "recipe_ingredients",
    primaryKeys = ["recipeId", "productId"]
)
data class RecipeIngredientCrossRef(
    val recipeId: Int,
    val productId: String
)