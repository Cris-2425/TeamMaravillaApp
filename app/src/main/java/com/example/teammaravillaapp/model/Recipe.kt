package com.example.teammaravillaapp.model

import androidx.annotation.DrawableRes

/**
 * Receta simple compuesta por un título, imagen y lista de ingredientes.
 *
 * @property title nombre de la receta.
 * @property imageRes imagen opcional asociada (foto/plato).
 * @property productIds ids de productos del catálogo (API/Room).
 */

data class Recipe(
    val id: Int,
    val title: String,
    @DrawableRes val imageRes: Int? = null,
    val productIds: List<String> = emptyList(),
    val instructions: String = ""
)