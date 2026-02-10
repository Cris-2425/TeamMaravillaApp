package com.example.teammaravillaapp.data.remote.mapper

import com.example.teammaravillaapp.model.ProductCategory

/**
 * Mapeo entre String de API y [ProductCategory] de dominio.
 *
 * - Se asegura que la conversión siempre devuelva un valor válido.
 * - Valores desconocidos o nulos caen en [ProductCategory.OTHER].
 * - Normaliza espacios, guiones y mayúsculas para robustez.
 */

/**
 * Convierte un string de categoría API a [ProductCategory].
 *
 * - Aplica heurística de normalización:
 *   - trim, lowercase, reemplaza espacios y guiones por "_"
 * - Fallback: cualquier valor desconocido -> [ProductCategory.OTHER]
 */
fun String?.toProductCategoryOrDefault(): ProductCategory {
    val key = this.normalizeKey() ?: return ProductCategory.OTHER
    return when (key) {
        "fruits", "fruit" -> ProductCategory.FRUITS
        "vegetables", "vegetable", "veggies" -> ProductCategory.VEGETABLES
        "dairy" -> ProductCategory.DAIRY
        "bakery" -> ProductCategory.BAKERY
        "meat" -> ProductCategory.MEAT
        "fish", "seafood" -> ProductCategory.FISH
        "drinks", "drink", "beverages" -> ProductCategory.DRINKS
        "pasta" -> ProductCategory.PASTA
        "rice" -> ProductCategory.RICE
        "cleaning" -> ProductCategory.CLEANING
        "other" -> ProductCategory.OTHER
        else -> ProductCategory.OTHER
    }
}

/**
 * Convierte un [ProductCategory] a su valor correspondiente en la API.
 *
 * - Útil para enviar datos al backend.
 */
fun ProductCategory.apiValue(): String = when (this) {
    ProductCategory.FRUITS -> "fruits"
    ProductCategory.VEGETABLES -> "vegetables"
    ProductCategory.DAIRY -> "dairy"
    ProductCategory.BAKERY -> "bakery"
    ProductCategory.MEAT -> "meat"
    ProductCategory.FISH -> "fish"
    ProductCategory.DRINKS -> "drinks"
    ProductCategory.PASTA -> "pasta"
    ProductCategory.RICE -> "rice"
    ProductCategory.CLEANING -> "cleaning"
    ProductCategory.OTHER -> "other"
}

/**
 * Normaliza la clave de categoría para comparación robusta.
 *
 * - Trim y lowercase.
 * - Reemplaza espacios y guiones por "_".
 * - Retorna null si queda vacía.
 */
private fun String?.normalizeKey(): String? =
    this?.trim()
        ?.lowercase()
        ?.replace(" ", "_")
        ?.replace("-", "_")
        ?.takeIf { it.isNotBlank() }