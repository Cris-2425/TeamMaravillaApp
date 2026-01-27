package com.example.teammaravillaapp.model

data class IngredientLine(
    val product: Product,
    val quantity: Double?,
    val unit: String?,
    val position: Int
)