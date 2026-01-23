package com.example.teammaravillaapp.data.local.mapper

import com.example.teammaravillaapp.data.local.entity.RecipeEntity
import com.example.teammaravillaapp.data.local.entity.RecipeWithProductsRoom
import com.example.teammaravillaapp.model.Recipe
import com.example.teammaravillaapp.model.RecipeWithIngredients

/**
 * Mappers de Recetas (Room -> Domain)
 *
 * IMPORTANTE:
 * - NO redefinir ProductEntity.toDomain() aquí. Ese mapper vive en ProductEntityMapper.kt
 */

fun RecipeEntity.toDomain(productIds: List<String>): Recipe =
    Recipe(
        id = id,
        title = title,
        imageRes = imageRes,
        instructions = instructions,
        productIds = productIds
    )

fun RecipeWithProductsRoom.toDomain(): RecipeWithIngredients =
    RecipeWithIngredients(
        recipe = Recipe(
            id = recipe.id,
            title = recipe.title,
            imageRes = recipe.imageRes,
            instructions = recipe.instructions,
            productIds = products.map { it.id }
        ),
        // aquí usamos el ProductEntity.toDomain() BUENO (del ProductEntityMapper)
        ingredients = products.map { it.toDomain() }
    )
