package com.example.teammaravillaapp.data.local.mapper

import com.example.teammaravillaapp.data.local.entity.ListEntity
import com.example.teammaravillaapp.model.ListBackground
import com.example.teammaravillaapp.model.UserList

fun UserList.toEntity(): ListEntity = ListEntity(
    id = id,
    name = name,
    background = background.name,
    productIds = productIds
)

fun ListEntity.toDomain(): UserList = UserList(
    id = id,
    name = name,
    background = ListBackground.valueOf(background),
    productIds = productIds
)