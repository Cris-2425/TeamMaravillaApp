package com.example.teammaravillaapp.data.seed

import androidx.annotation.DrawableRes
import com.example.teammaravillaapp.model.ProductCategory

data class SeedItem(
    val id: String,
    val name: String,
    val category: ProductCategory,
    @DrawableRes val imageRes: Int? = null
)