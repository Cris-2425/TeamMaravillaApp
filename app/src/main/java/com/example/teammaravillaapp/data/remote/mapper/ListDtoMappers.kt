package com.example.teammaravillaapp.data.remote.mapper

import com.example.teammaravillaapp.data.remote.dto.ListItemDto
import com.example.teammaravillaapp.data.remote.dto.UserListDto
import com.example.teammaravillaapp.model.ListBackground
import com.example.teammaravillaapp.model.ListItemSnapshot
import com.example.teammaravillaapp.model.UserList
import com.example.teammaravillaapp.model.UserListSnapshot

fun UserListDto.toDomainSimple(): UserList =
    UserList(
        id = id,
        name = name,
        background = background.toListBackgroundOrDefault(),
        productIds = items.sortedBy { it.position }.map { it.productId }
    )

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

fun ListItemDto.toSnapshot(): ListItemSnapshot =
    ListItemSnapshot(
        productId = productId,
        addedAt = addedAt,
        position = position,
        checked = checked,
        quantity = quantity
    )

fun ListItemSnapshot.toDto(): ListItemDto =
    ListItemDto(
        productId = productId,
        addedAt = addedAt,
        position = position,
        checked = checked,
        quantity = quantity
    )

private fun String.toListBackgroundOrDefault(): ListBackground =
    runCatching { ListBackground.valueOf(this) }
        .getOrElse { ListBackground.FONDO1 }