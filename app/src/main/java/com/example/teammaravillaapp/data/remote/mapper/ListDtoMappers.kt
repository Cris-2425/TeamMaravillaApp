package com.example.teammaravillaapp.data.remote.mapper

import com.example.teammaravillaapp.data.remote.dto.ListItemDto
import com.example.teammaravillaapp.data.remote.dto.UserListDto
import com.example.teammaravillaapp.data.remote.dto.RecipeIngredientLineDto
import com.example.teammaravillaapp.model.*

/**
 * Mapeo entre DTOs de API y modelos de dominio para:
 * - Listas de usuario ([UserList], [UserListSnapshot], [ListItemSnapshot])
 * - Líneas de ingredientes ([IngredientLine])
 *
 * Estrategia:
 * - Centralizar conversiones entre capas para mantener la lógica consistente.
 * - Proveer fallbacks seguros en caso de datos incompletos o inválidos.
 * - Ordenar elementos por `position` para mantener consistencia en UI y sincronización.
 */

/**
 * Convierte un [UserListDto] en un [UserList] simplificado para UI.
 *
 * - `productIds` se extraen de los items y se ordenan por `position`.
 * - `background` se convierte mediante [toListBackgroundOrDefault].
 * - Útil para mostrar listas sin necesidad de detalles de items.
 */
fun UserListDto.toDomainSimple(): UserList =
    UserList(
        id = id,
        name = name,
        background = background.toListBackgroundOrDefault(),
        productIds = items.sortedBy { it.position }.map { it.productId }
    )

/**
 * Convierte un [UserListDto] en [UserListSnapshot] completo.
 *
 * - Incluye todos los items con estado, posición, cantidad, etc.
 * - Se usa para sincronización con backend o persistencia local completa.
 */
fun UserListDto.toSnapshot(): UserListSnapshot =
    UserListSnapshot(
        id = id,
        name = name,
        background = background.toListBackgroundOrDefault(),
        createdAt = createdAt,
        items = items
            .sortedBy { it.position }
            .map { it.toSnapshot() }
    )

/**
 * Convierte un [UserListSnapshot] en [UserListDto] listo para API.
 *
 * - Convierte `background` a string.
 * - Mantiene orden de items según `position`.
 */
fun UserListSnapshot.toDto(): UserListDto =
    UserListDto(
        id = id,
        name = name,
        background = background.name,
        createdAt = createdAt,
        items = items
            .sortedBy { it.position }
            .map { it.toDto() }
    )

/**
 * Convierte un [ListItemDto] en [ListItemSnapshot] para dominio.
 *
 * - Se preservan `productId`, `addedAt`, `position`, `checked` y `quantity`.
 * - Se usa principalmente en sincronización de listas y UI.
 */
fun ListItemDto.toSnapshot(): ListItemSnapshot =
    ListItemSnapshot(
        productId = productId,
        addedAt = addedAt,
        position = position,
        checked = checked,
        quantity = quantity
    )

/**
 * Convierte un [ListItemSnapshot] en [ListItemDto] listo para API.
 */
fun ListItemSnapshot.toDto(): ListItemDto =
    ListItemDto(
        productId = productId,
        addedAt = addedAt,
        position = position,
        checked = checked,
        quantity = quantity
    )

/**
 * Convierte un string en [ListBackground], usando un fallback seguro.
 *
 * - Si la cadena no coincide con ningún valor de [ListBackground], retorna [ListBackground.FONDO1].
 */
private fun String.toListBackgroundOrDefault(): ListBackground =
    runCatching { ListBackground.valueOf(this) }
        .getOrElse { ListBackground.FONDO1 }