package com.example.teammaravillaapp.model

import androidx.annotation.DrawableRes

data class Recipe(
    val title: String,
    @DrawableRes val imageRes: Int? = null,
    val products: List<Product> = emptyList()
)