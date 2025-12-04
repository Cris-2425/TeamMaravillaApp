package com.example.teammaravillaapp.model

import androidx.annotation.DrawableRes

/**
 * Receta simple compuesta por un título, imagen y lista de ingredientes.
 *
 * @property title nombre de la receta.
 * @property imageRes imagen opcional asociada (foto/plato).
 * @property products ingredientes necesarios (productos del catálogo).
 */
data class Recipe(
    val id: Int,
    val title: String,
    @DrawableRes val imageRes: Int? = null,
    val products: List<Product> = emptyList(),
    val instructions: String = ""
)