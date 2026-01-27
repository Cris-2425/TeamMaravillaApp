package com.example.teammaravillaapp.data.local.mapper

import com.example.teammaravillaapp.data.local.entity.RecipeEntity
import com.example.teammaravillaapp.data.local.entity.RecipeWithProductsRoom
import com.example.teammaravillaapp.model.Recipe
import com.example.teammaravillaapp.model.RecipeWithIngredients

/**
 * Mappers de Recetas (Room -> Domain)
 *
 * IMPORTANTE:
 * - NO convertir null -> 0 (eso provoca painterResource(0) y crasheos)
 */

fun RecipeEntity.toDomain(productIds: List<String>): Recipe =
    Recipe(
        id = id,
        title = title,
        imageRes = imageRes,          // ✅ dejamos null si no hay imagen
        instructions = instructions,
        productIds = productIds
    )

fun RecipeWithProductsRoom.toDomain(): RecipeWithIngredients =
    RecipeWithIngredients(
        recipe = Recipe(
            id = recipe.id,
            title = recipe.title,
            imageRes = recipe.imageRes, // ✅ dejamos null si no hay imagen
            instructions = recipe.instructions,
            productIds = products.map { it.id }
        ),
        ingredients = products.map { it.toDomain() } // tu mapper de ProductEntity bueno
    )
