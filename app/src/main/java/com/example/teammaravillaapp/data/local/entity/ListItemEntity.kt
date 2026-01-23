package com.example.teammaravillaapp.data.local.entity

import androidx.room.Entity

@Entity(
    tableName = "list_items",
    primaryKeys = ["listId", "productId"]
)
data class ListItemEntity(
    val listId: String,
    val productId: String,
    val addedAt: Long,
    val position: Int,
    val checked: Boolean = false,
    val quantity: Int = 1
)