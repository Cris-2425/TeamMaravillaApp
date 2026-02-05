package com.example.teammaravillaapp.model

data class UserListSnapshot(
    val id: String,
    val name: String,
    val background: ListBackground,
    val createdAt: Long,
    val items: List<ListItemSnapshot>
)

data class ListItemSnapshot(
    val productId: String,
    val addedAt: Long,
    val position: Int,
    val checked: Boolean,
    val quantity: Int
)