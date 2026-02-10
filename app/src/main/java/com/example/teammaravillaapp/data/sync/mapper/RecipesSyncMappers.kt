package com.example.teammaravillaapp.data.sync.mapper

import com.example.teammaravillaapp.data.local.entity.RecipeEntity
import com.example.teammaravillaapp.data.local.entity.RecipeIngredientsCrossRef
import com.example.teammaravillaapp.data.remote.dto.RecipeDto
import com.example.teammaravillaapp.data.remote.dto.RecipeIngredientDto

/**
 * Mapper de sincronización entre la capa remota (DTOs) y la capa local (Entities).
 *
 * Este archivo representa el **único punto de conversión** entre:
 * - Modelos provenientes de la API (DTO)
 * - Modelos persistidos en la base de datos local (Room Entities)
 *
 * ### Principios aplicados
 * - **Single Responsibility**: cada función realiza una conversión específica.
 * - **Centralización de mapeos**: evita duplicación y conversiones dispersas.
 * - **Desacoplamiento**: la capa de datos puede evolucionar sin impactar dominio o UI.
 *
 * Importante:
 * No debe existir lógica de negocio aquí. Solo transformación de datos.
 */

/**
 * Convierte un [RecipeDto] proveniente de la API en un [RecipeEntity] para persistencia local.
 *
 * Se mapean únicamente los campos necesarios para la tabla `recipes`.
 * Las relaciones (ingredientes) se manejan por separado mediante una tabla de cruce.
 *
 * @return [RecipeEntity] listo para ser almacenado en Room.
 */
fun RecipeDto.toEntity(): RecipeEntity =
    RecipeEntity(
        id = id,
        title = title,
        imageRes = imageRes,
        instructions = instructions
    )

/**
 * Convierte un [RecipeIngredientDto] en una relación [RecipeIngredientsCrossRef].
 *
 * Esta entidad representa una **tabla many-to-many** entre recetas e ingredientes,
 * permitiendo normalización y consultas eficientes en Room.
 *
 * @param recipeId ID de la receta a la que pertenece el ingrediente.
 * @return relación lista para ser persistida en la base de datos local.
 */
fun RecipeIngredientDto.toEntity(recipeId: Int): RecipeIngredientsCrossRef =
    RecipeIngredientsCrossRef(
        recipeId = recipeId,
        productId = productId,
        quantity = quantity,
        unit = unit,
        position = position
    )

/**
 * Convierte un [RecipeEntity] junto con sus ingredientes en un [RecipeDto].
 *
 * Este mapper se utiliza principalmente para:
 * - Reconstruir el modelo de dominio/remoto desde datos locales
 * - Enviar información consistente a capas superiores (Domain / UI)
 *
 * `imageUrl` se establece en `null` ya que no se persiste localmente
 * y puede ser resuelto posteriormente desde la API o capa de presentación.
 *
 * @param ingredients lista de ingredientes asociados a la receta.
 * @return [RecipeDto] completo.
 */
fun RecipeEntity.toDto(ingredients: List<RecipeIngredientDto>): RecipeDto =
    RecipeDto(
        id = id,
        title = title,
        instructions = instructions,
        imageUrl = null,
        imageRes = imageRes,
        ingredients = ingredients
    )

/**
 * Convierte una relación [RecipeIngredientsCrossRef] en un [RecipeIngredientDto].
 *
 * Usado al reconstruir el estado de una receta desde la base de datos local
 * para su consumo por capas superiores.
 *
 * @return DTO del ingrediente con información de cantidad y posición.
 */
fun RecipeIngredientsCrossRef.toDto(): RecipeIngredientDto =
    RecipeIngredientDto(
        productId = productId,
        quantity = quantity,
        unit = unit,
        position = position
    )