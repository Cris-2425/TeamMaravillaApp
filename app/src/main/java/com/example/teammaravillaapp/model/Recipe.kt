package com.example.teammaravillaapp.model

data class Recipe(
    val title: String,
    val imageRes: Int? = null,
    val products: List<Product> = emptyList()
)