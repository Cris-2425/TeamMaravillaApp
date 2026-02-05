package com.example.teammaravillaapp.data.sync.mapper

import com.example.teammaravillaapp.data.local.entity.RecipeEntity
import com.example.teammaravillaapp.data.local.entity.RecipeIngredientsCrossRef
import com.example.teammaravillaapp.data.remote.dto.RecipeDto
import com.example.teammaravillaapp.data.remote.dto.RecipeIngredientDto

/**
 * DTO (API) <-> Entity (Room)
 * ÚNICO sitio para estas conversiones (evita overloads).
 */

fun RecipeDto.toEntity(): RecipeEntity =
    RecipeEntity(
        id = id,
        title = title,
        imageRes = imageRes,
        instructions = instructions
    )

fun RecipeIngredientDto.toEntity(recipeId: Int): RecipeIngredientsCrossRef =
    RecipeIngredientsCrossRef(
        recipeId = recipeId,
        productId = productId,
        quantity = quantity,
        unit = unit,
        position = position
    )

fun RecipeEntity.toDto(ingredients: List<RecipeIngredientDto>): RecipeDto =
    RecipeDto(
        id = id,
        title = title,
        instructions = instructions,
        imageUrl = null,
        imageRes = imageRes,
        ingredients = ingredients
    )

fun RecipeIngredientsCrossRef.toDto(): RecipeIngredientDto =
    RecipeIngredientDto(
        productId = productId,
        quantity = quantity,
        unit = unit,
        position = position
    )