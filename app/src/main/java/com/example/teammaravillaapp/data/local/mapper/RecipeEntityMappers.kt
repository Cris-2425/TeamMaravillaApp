package com.example.teammaravillaapp.data.local.mapper

import com.example.teammaravillaapp.data.local.entity.RecipeIngredientLineRoom
import com.example.teammaravillaapp.data.local.entity.RecipeWithProductsRoom
import com.example.teammaravillaapp.data.remote.mapper.toProductCategoryOrDefault
import com.example.teammaravillaapp.model.IngredientLine
import com.example.teammaravillaapp.model.Product
import com.example.teammaravillaapp.model.Recipe
import com.example.teammaravillaapp.model.RecipeWithIngredients

/**
 * Convierte una proyección Room (`RecipeWithProductsRoom`) en el modelo de dominio
 * [RecipeWithIngredients].
 *
 * ### Responsabilidad
 * - Aísla la capa de dominio de detalles de persistencia (Room).
 * - Reconstruye la entidad agregada de dominio:
 *   - `Recipe`
 *   - Lista tipada de `IngredientLine`
 *
 * ### Decisiones de diseño
 * - `productIds` se derivan de `products.map { it.id }` para mantener coherencia con
 *   el modelo de dominio basado en referencias.
 * - El orden se preserva según venga de la consulta Room.
 *
 * Esta función es **pura** (sin efectos secundarios) y segura para uso en cualquier hilo.
 *
 * @receiver Proyección Room que contiene receta + productos.
 * @return Agregado de dominio listo para uso en UI o lógica de negocio.
 *
 * @see RecipeWithIngredients
 */
fun RecipeWithProductsRoom.toDomain(): RecipeWithIngredients =
    RecipeWithIngredients(
        recipe = Recipe(
            id = recipe.id,
            title = recipe.title,
            imageRes = recipe.imageRes,
            instructions = recipe.instructions,
            productIds = products.map { it.id }
        ),
        ingredients = products.map { it.toDomain() }
    )

/**
 * Convierte una línea plana de Room ([RecipeIngredientLineRoom]) en un
 * modelo de dominio [IngredientLine].
 *
 * ### Conversión relevante
 * - `category` se transforma usando `toProductCategoryOrDefault()` para evitar
 *   estados inválidos en dominio si el valor persistido no coincide con el enum esperado.
 *
 * ### Seguridad
 * Función **pura** y determinista: no accede a IO ni estado externo.
 *
 * @receiver Línea de ingrediente proveniente de Room.
 * @return Modelo de dominio equivalente.
 *
 * @see IngredientLine
 * @see Product
 */
fun RecipeIngredientLineRoom.toDomain(): IngredientLine =
    IngredientLine(
        product = Product(
            id = productId,
            name = name,
            imageRes = imageRes,
            category = category.toProductCategoryOrDefault(),
            imageUrl = imageUrl
        ),
        quantity = quantity,
        unit = unit,
        position = position
    )