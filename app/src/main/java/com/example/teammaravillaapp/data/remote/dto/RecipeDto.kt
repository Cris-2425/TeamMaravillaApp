package com.example.teammaravillaapp.data.remote.dto

data class RecipeDto(
    val id: Int,
    val title: String,
    val instructions: String,
    val imageUrl: String?,
    val imageRes: Int?,
    val ingredients: List<RecipeIngredientDto>
)