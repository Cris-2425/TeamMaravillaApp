package com.example.teammaravillaapp.model

import androidx.annotation.DrawableRes

/**
 * Modelo de producto básico del catálogo de la app.
 *
 * Este modelo representa un producto que puede ser mostrado en listas,
 * recetas o pantallas de selección. Es utilizado principalmente en la
 * **capa de dominio y presentación**, mientras que su origen de datos
 * se encuentra en `ProductData` o repositorios.
 *
 * Ejemplo de uso:
 * ```kotlin
 * val manzana = Product(id = "1", name = "Manzana", category = ProductCategory.FRUITS)
 * ```
 *
 * @property id Identificador único del producto (puede ser UUID o clave en base de datos).
 * @property name Nombre legible del producto (clave natural en `ProductData`).
 * @property imageRes Recurso drawable opcional para la imagen del producto.
 * @property category Categoría del producto (usada para filtrado y UI).
 * @property imageUrl URL opcional de imagen remota (si existe en API externa).
 */
data class Product(
    val id: String,
    val name: String,
    val imageRes: Int? = null,
    val category: ProductCategory = ProductCategory.OTHER,
    val imageUrl: String? = null
)