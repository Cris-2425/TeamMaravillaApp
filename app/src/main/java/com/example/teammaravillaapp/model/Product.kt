package com.example.teammaravillaapp.model

data class Product(
    val name: String,
    val imageRes: Int? = null,
    val category: ProductCategory = ProductCategory.OTHER
)