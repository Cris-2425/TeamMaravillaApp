package com.example.teammaravillaapp.data.local.mapper

import com.example.teammaravillaapp.data.local.entity.ListEntity
import com.example.teammaravillaapp.data.local.entity.ListItemEntity
import com.example.teammaravillaapp.data.local.entity.ListWithItemsRoom
import com.example.teammaravillaapp.model.ListBackground
import com.example.teammaravillaapp.model.ListItemSnapshot
import com.example.teammaravillaapp.model.UserList
import com.example.teammaravillaapp.model.UserListSnapshot

/**
 * Convierte un [UserList] de la capa de dominio a [ListEntity] para persistencia en Room.
 *
 * @param createdAt Timestamp de creación. Por defecto, el momento actual.
 * @return [ListEntity] listo para almacenamiento.
 */
fun UserList.toEntity(createdAt: Long = System.currentTimeMillis()): ListEntity =
    ListEntity(
        id = id,
        name = name,
        background = background.name,
        createdAt = createdAt
    )

/**
 * Convierte un [ListEntity] y sus IDs de productos asociados a un [UserList] de dominio.
 *
 * @param productIds Lista de IDs de productos asociados.
 * @return [UserList] de la capa de dominio.
 */
fun ListEntity.toDomain(productIds: List<String>): UserList =
    UserList(
        id = id,
        name = name,
        background = background.toListBackgroundOrDefault(),
        productIds = productIds
    )

/**
 * Convierte un [ListWithItemsRoom] (Room) a un [UserList] de dominio,
 * ordenando los elementos según su posición.
 */
fun ListWithItemsRoom.toDomain(): UserList =
    list.toDomain(
        productIds = items.sortedBy { it.position }.map { it.productId }
    )

/**
 * Convierte un [ListWithItemsRoom] a [UserListSnapshot] para UI o snapshots inmutables.
 */
fun ListWithItemsRoom.toSnapshot(): UserListSnapshot =
    UserListSnapshot(
        id = list.id,
        name = list.name,
        background = list.background.toListBackgroundOrDefault(),
        createdAt = list.createdAt,
        items = items.sortedBy { it.position }.map { it.toSnapshot() }
    )

/**
 * Convierte un [ListItemEntity] a [ListItemSnapshot] para snapshots de UI.
 */
fun ListItemEntity.toSnapshot(): ListItemSnapshot =
    ListItemSnapshot(
        productId = productId,
        addedAt = addedAt,
        position = position,
        checked = checked,
        quantity = quantity
    )

/**
 * Convierte un string a [ListBackground] seguro, devolviendo un valor por defecto
 * si no coincide con ninguna constante del enum.
 */
private fun String.toListBackgroundOrDefault(): ListBackground =
    runCatching { ListBackground.valueOf(this) }
        .getOrElse { ListBackground.FONDO2 }