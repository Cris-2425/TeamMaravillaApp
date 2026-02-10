package com.example.teammaravillaapp.data.remote.dto

/**
 * DTO que representa un ingrediente básico de una receta para sincronización con el backend.
 *
 * Se utiliza dentro de [RecipeDto] y normalmente no contiene datos de presentación como nombre o imagen.
 *
 * @property productId Identificador del producto.
 * @property quantity Cantidad del ingrediente (opcional).
 * @property unit Unidad de medida (opcional).
 * @property position Posición del ingrediente dentro de la receta.
 */
data class RecipeIngredientDto(
    val productId: String,
    val quantity: Double?,
    val unit: String?,
    val position: Int
)