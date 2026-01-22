package com.example.teammaravillaapp.model

data class RecipeWithIngredients(
    val recipe: Recipe,
    val ingredients: List<Product>
)