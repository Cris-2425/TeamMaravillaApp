package com.example.teammaravillaapp.model

import androidx.annotation.DrawableRes

data class Product(
    val name: String,
    @DrawableRes val imageRes: Int? = null,
    val category: ProductCategory = ProductCategory.OTHER
)