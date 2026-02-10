package com.example.teammaravillaapp.model

/**
 * Representa una línea de ingrediente en una receta o lista de compras.
 *
 * Este modelo es usado principalmente en la **capa de dominio y presentación**, para
 * mostrar productos con su cantidad y unidad en la UI.
 *
 * @property product Producto asociado a esta línea. Debe contener información como nombre, id y otros atributos relevantes.
 * @property quantity Cantidad del producto. Puede ser `null` si no se especifica cantidad.
 * @property unit Unidad de medida asociada a la cantidad (por ejemplo, "kg", "l", "u"). Puede ser `null`.
 * @property position Posición de la línea dentro de la lista de ingredientes. Útil para ordenamiento en UI o persistencia.
 */
data class IngredientLine(
    val product: Product,
    val quantity: Double? = null,
    val unit: String? = null,
    val position: Int
)