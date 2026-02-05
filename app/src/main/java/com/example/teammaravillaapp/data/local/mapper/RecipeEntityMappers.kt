package com.example.teammaravillaapp.data.local.mapper

import com.example.teammaravillaapp.data.local.entity.RecipeWithProductsRoom
import com.example.teammaravillaapp.model.Recipe
import com.example.teammaravillaapp.model.RecipeWithIngredients

fun RecipeWithProductsRoom.toDomain(): RecipeWithIngredients =
    RecipeWithIngredients(
        recipe = Recipe(
            id = recipe.id,
            title = recipe.title,
            imageRes = recipe.imageRes,
            instructions = recipe.instructions,
            productIds = products.map { it.id }
        ),
        ingredients = products.map { it.toDomain() }
    )