package com.example.teammaravillaapp.data.local.mapper

import com.example.teammaravillaapp.data.local.entity.ListEntity
import com.example.teammaravillaapp.data.local.entity.ListWithItemsRoom
import com.example.teammaravillaapp.model.ListBackground
import com.example.teammaravillaapp.model.UserList

fun UserList.toEntity(
    createdAt: Long = System.currentTimeMillis()
): ListEntity = ListEntity(
    id = id,
    name = name,
    background = background.name,
    createdAt = createdAt
)

fun ListEntity.toDomain(productIds: List<String>): UserList = UserList(
    id = id,
    name = name,
    background = ListBackground.valueOf(background),
    productIds = productIds
)

fun ListWithItemsRoom.toDomain(): UserList =
    list.toDomain(
        productIds = items
            .sortedBy { it.position }
            .map { it.productId }
    )