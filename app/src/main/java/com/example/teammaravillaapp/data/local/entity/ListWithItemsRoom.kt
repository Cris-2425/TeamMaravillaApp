package com.example.teammaravillaapp.data.local.entity

import androidx.room.Embedded
import androidx.room.Relation

data class ListWithItemsRoom(
    @Embedded val list: ListEntity,

    @Relation(
        parentColumn = "id",
        entityColumn = "listId"
    )
    val items: List<ListItemEntity>
)