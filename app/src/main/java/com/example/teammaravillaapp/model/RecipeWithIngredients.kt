package com.example.teammaravillaapp.model

/**
 * Modelo compuesto que combina una receta con sus ingredientes concretos.
 *
 * Este modelo es útil en **UI y ViewModels** para mostrar recetas con
 * objetos completos de `Product` en lugar de solo IDs.
 *
 * Ejemplo de uso:
 * ```kotlin
 * val recipeWithIngredients = RecipeWithIngredients(
 *     recipe = receta,
 *     ingredients = listOf(Manzana, Platano, Kiwi)
 * )
 * ```
 *
 * @property recipe Objeto de tipo [Recipe].
 * @property ingredients Lista de productos concretos asociados a la receta.
 */
data class RecipeWithIngredients(
    val recipe: Recipe,
    val ingredients: List<Product>
)