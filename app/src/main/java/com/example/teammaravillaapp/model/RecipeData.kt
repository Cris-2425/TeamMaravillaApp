package com.example.teammaravillaapp.model

import com.example.teammaravillaapp.R

/**
 * Datos de ejemplo para la sección de Recetas.
 *
 * Se apoya en los alias de `ProductAliases.kt` para
 * construir ingredientes tipados sin errores de nombre.
 */
object RecipeData {

    /** Listado de recetas de muestra para UI. */
    val recipes = listOf(
        Recipe(
            title = "Tortilla de Patata",
            imageRes = R.drawable.tortilla_de_patata,
            products = listOf(Patata, Huevo, Cebolla, Aceite)
        ),
        Recipe(
            title = "Ensalada Mixta",
            imageRes = R.drawable.ensalada,
            products = listOf(Lechuga, Tomate, Cebolla, Aceitunas)
        ),
        Recipe(
            title = "Pasta al Pesto",
            imageRes = R.drawable.pasta_con_pesto,
            products = listOf(Pasta, Pesto, Queso)
        ),
        Recipe(
            title = "Bizcocho Casero",
            imageRes = R.drawable.bizcocho,
            products = listOf(Harina, Huevo, Azucar, Leche)
        ),
        Recipe(
            title = "Smoothie de Plátano y Mango",
            imageRes = R.drawable.smoothie_platano_mango,
            products = listOf(Platano, Mango, Yogur)
        )
    )
}