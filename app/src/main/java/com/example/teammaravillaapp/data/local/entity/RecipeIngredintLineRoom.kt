package com.example.teammaravillaapp.data.local.entity

/**
 * Proyección plana de una línea de ingrediente resultante de una `JOIN`
 * entre `recipe_ingredients` y `products`.
 *
 * No es una entidad persistida como tabla independiente, sino un **DTO de consulta**
 * usado por Room para devolver datos listos para consumo en capa de dominio/UI.
 *
 * ### Diseño
 * - Incluye tanto metadatos del producto (`name`, `category`, `imageUrl`, `imageRes`)
 *   como atributos propios de la relación receta-producto (`quantity`, `unit`, `position`).
 * - `position` garantiza un orden estable en renderizado.
 *
 * @property productId ID del producto asociado.
 * @property name Nombre del producto.
 * @property category Categoría serializada como `String` (se convierte a dominio vía mapper).
 * @property imageUrl URL remota de la imagen (opcional).
 * @property imageRes Recurso drawable local (opcional).
 * @property quantity Cantidad asociada al ingrediente (puede ser `null`).
 * @property unit Unidad de medida (puede ser `null`).
 * @property position Posición del ingrediente dentro de la receta.
 */
data class RecipeIngredientLineRoom(
    val productId: String,
    val name: String,
    val category: String,
    val imageUrl: String?,
    val imageRes: Int?,
    val quantity: Double?,
    val unit: String?,
    val position: Int
)