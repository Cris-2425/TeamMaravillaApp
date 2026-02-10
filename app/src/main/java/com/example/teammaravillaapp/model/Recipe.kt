package com.example.teammaravillaapp.model

import androidx.annotation.DrawableRes

/**
 * Modelo de receta simple, compuesto por título, imagen y lista de productos.
 *
 * Este modelo se utiliza tanto en **capa de dominio** como en **UI** para mostrar recetas
 * y relacionarlas con los productos disponibles en el catálogo.
 *
 * Ejemplo de uso:
 * ```kotlin
 * val receta = Recipe(
 *     id = 1,
 *     title = "Ensalada de frutas",
 *     productIds = listOf("Manzana", "Plátano", "Kiwi"),
 *     instructions = "Cortar y mezclar todos los ingredientes.",
 *     imageRes = R.drawable.ensalada_frutas
 * )
 * ```
 *
 * @property id Identificador único de la receta.
 * @property title Nombre de la receta.
 * @property productIds Lista de IDs de productos del catálogo (`Product.id`) asociados a la receta.
 * @property instructions Instrucciones paso a paso para preparar la receta.
 * @property imageRes Recurso drawable opcional de la receta.
 * @property imageUrl URL de imagen remota opcional.
 */
data class Recipe(
    val id: Int,
    val title: String,
    val productIds: List<String>,
    val instructions: String,
    val imageRes: Int? = null,
    val imageUrl: String? = null
)