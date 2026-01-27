package com.example.teammaravillaapp.data.local.mapper

import com.example.teammaravillaapp.model.IngredientLine
import com.example.teammaravillaapp.model.Product
import com.example.teammaravillaapp.model.ProductCategory
import com.example.teammaravillaapp.network.dto.RecipeIngredientLine

fun RecipeIngredientLine.toIngredientLine(): IngredientLine =
    IngredientLine(
        product = Product(
            id = productId,
            name = name,
            imageRes = null,
            category = runCatching { ProductCategory.valueOf(category) }
                .getOrElse { ProductCategory.OTHER },
            imageUrl = imageUrl
        ),
        quantity = quantity,
        unit = unit,
        position = position
    )