package com.example.teammaravillaapp.data.remote.dto

data class RecipeIngredientDto(
    val productId: String,
    val quantity: Double?,
    val unit: String?,
    val position: Int
)