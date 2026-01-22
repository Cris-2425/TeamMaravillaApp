package com.example.teammaravillaapp.data.local.entity

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

// Relación RecipeEntity <-> ProductEntity (por tabla puente)
data class RecipeWithProductsRoom(
    @Embedded val recipe: RecipeEntity,

    @Relation(
        parentColumn = "id",
        entityColumn = "id",
        associateBy = Junction(
            value = RecipeIngredientCrossRef::class,
            parentColumn = "recipeId",
            entityColumn = "productId"
        )
    )
    val products: List<ProductEntity>
)