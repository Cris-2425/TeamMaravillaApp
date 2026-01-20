package com.example.teammaravillaapp.model

import androidx.annotation.DrawableRes

/**
 * Producto básico del catálogo.
 *
 * @property name nombre legible (clave natural en `ProductData`).
 * @property imageRes imagen opcional del producto.
 * @property category categoría a la que pertenece.
 */

data class Product(
    val id: String,
    val name: String,
    val imageRes: Int? = null,
    val category: ProductCategory? = ProductCategory.OTHER,
    val imageUrl: String? = null
)