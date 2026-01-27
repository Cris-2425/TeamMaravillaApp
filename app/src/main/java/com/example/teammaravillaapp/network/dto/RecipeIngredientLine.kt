package com.example.teammaravillaapp.network.dto

data class RecipeIngredientLine(
    val productId: String,
    val name: String,
    val category: String,
    val imageUrl: String?,
    val quantity: Double?,
    val unit: String?,
    val position: Int
)