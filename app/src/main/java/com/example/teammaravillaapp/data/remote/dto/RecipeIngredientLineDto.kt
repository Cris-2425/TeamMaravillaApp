package com.example.teammaravillaapp.data.remote.dto

data class RecipeIngredientLineDto(
    val productId: String,
    val name: String,
    val category: String,
    val imageUrl: String?,
    val quantity: Double?,
    val unit: String?,
    val position: Int
)