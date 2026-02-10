package com.example.teammaravillaapp.data.remote.dto
import com.example.teammaravillaapp.model.IngredientLine
import com.example.teammaravillaapp.model.ProductCategory
/**
 * DTO que representa una línea de ingrediente en una receta desde la API.
 *
 * Se utiliza principalmente en la capa de presentación y mapeo al dominio ([IngredientLine]).
 *
 * @property productId Identificador del producto asociado al ingrediente.
 * @property name Nombre del producto (para mostrar en UI).
 * @property category Categoría del producto (string que se mapea a [ProductCategory]).
 * @property imageUrl URL pública de la imagen del producto (si existe).
 * @property quantity Cantidad del ingrediente (opcional, puede ser null).
 * @property unit Unidad de medida del ingrediente (opcional, puede ser null).
 * @property position Posición del ingrediente en la receta (para ordenamiento).
 */
data class RecipeIngredientLineDto(
    val productId: String,
    val name: String,
    val category: String,
    val imageUrl: String?,
    val quantity: Double?,
    val unit: String?,
    val position: Int
)