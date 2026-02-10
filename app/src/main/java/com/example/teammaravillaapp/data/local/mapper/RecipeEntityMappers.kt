package com.example.teammaravillaapp.data.local.mapper

import com.example.teammaravillaapp.data.local.entity.RecipeWithProductsRoom
import com.example.teammaravillaapp.model.Recipe
import com.example.teammaravillaapp.model.RecipeWithIngredients

/**
 * Convierte un [RecipeWithProductsRoom] (Room) a [RecipeWithIngredients] de dominio.
 *
 * Incluye:
 * - La receta principal ([Recipe]).
 * - La lista de productos/ingredientes asociados.
 *
 * Se mapean los IDs de los productos dentro de la receta y se preserva la lista completa
 * de ingredientes para UI o lógica de negocio.
 */
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