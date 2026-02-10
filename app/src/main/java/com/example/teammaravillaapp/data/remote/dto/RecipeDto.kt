package com.example.teammaravillaapp.data.remote.dto
import com.example.teammaravillaapp.model.RecipeWithIngredients
/**
 * DTO que representa una receta desde la API.
 *
 * Se usa para:
 * - Obtener recetas del backend.
 * - Enviar recetas al backend durante sincronización.
 * - Mapear a la capa de dominio ([RecipeWithIngredients]).
 *
 * @property id Identificador único de la receta.
 * @property title Título o nombre de la receta.
 * @property instructions Instrucciones completas para preparar la receta.
 * @property imageUrl URL pública de la imagen de la receta (si existe).
 * @property imageRes Recurso local de imagen en caso de ser una receta local (opcional).
 * @property ingredients Lista de ingredientes básicos ([RecipeIngredientDto]) asociados a la receta.
 */
data class RecipeDto(
    val id: Int,
    val title: String,
    val instructions: String,
    val imageUrl: String?,
    val imageRes: Int?,
    val ingredients: List<RecipeIngredientDto>
)