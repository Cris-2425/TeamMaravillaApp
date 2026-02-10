package com.example.teammaravillaapp.data.remote.mapper

import com.example.teammaravillaapp.data.remote.dto.RecipeIngredientLineDto
import com.example.teammaravillaapp.model.IngredientLine
import com.example.teammaravillaapp.model.Product

/**
 * Convierte un [RecipeIngredientLineDto] en [IngredientLine] (domain).
 *
 * - Incluye la creación de [Product] parcial para cada línea.
 * - `category` se convierte con [toProductCategoryOrDefault].
 * - `imageRes` se inicializa como null porque es local.
 */
fun RecipeIngredientLineDto.toDomain(): IngredientLine =
    IngredientLine(
        product = Product(
            id = productId,
            name = name,
            imageRes = null,
            category = category.toProductCategoryOrDefault(),
            imageUrl = imageUrl
        ),
        quantity = quantity,
        unit = unit,
        position = position
    )