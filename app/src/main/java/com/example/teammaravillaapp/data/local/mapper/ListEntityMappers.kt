package com.example.teammaravillaapp.data.local.mapper

import com.example.teammaravillaapp.data.local.entity.ListEntity
import com.example.teammaravillaapp.data.local.entity.ListItemEntity
import com.example.teammaravillaapp.data.local.entity.ListWithItemsRoom
import com.example.teammaravillaapp.model.ListBackground
import com.example.teammaravillaapp.model.ListItemSnapshot
import com.example.teammaravillaapp.model.UserList
import com.example.teammaravillaapp.model.UserListSnapshot

fun UserList.toEntity(createdAt: Long = System.currentTimeMillis()): ListEntity =
    ListEntity(
        id = id,
        name = name,
        background = background.name,
        createdAt = createdAt
    )

fun ListEntity.toDomain(productIds: List<String>): UserList =
    UserList(
        id = id,
        name = name,
        background = background.toListBackgroundOrDefault(),
        productIds = productIds
    )

fun ListWithItemsRoom.toDomain(): UserList =
    list.toDomain(
        productIds = items.sortedBy { it.position }.map { it.productId }
    )

fun ListWithItemsRoom.toSnapshot(): UserListSnapshot =
    UserListSnapshot(
        id = list.id,
        name = list.name,
        background = list.background.toListBackgroundOrDefault(),
        createdAt = list.createdAt,
        items = items.sortedBy { it.position }.map { it.toSnapshot() }
    )

fun ListItemEntity.toSnapshot(): ListItemSnapshot =
    ListItemSnapshot(
        productId = productId,
        addedAt = addedAt,
        position = position,
        checked = checked,
        quantity = quantity
    )

private fun String.toListBackgroundOrDefault(): ListBackground =
    runCatching { ListBackground.valueOf(this) }
        .getOrElse { ListBackground.FONDO2 }