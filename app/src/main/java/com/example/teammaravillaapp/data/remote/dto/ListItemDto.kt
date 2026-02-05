package com.example.teammaravillaapp.data.remote.dto

data class ListItemDto(
    val productId: String,
    val addedAt: Long,
    val position: Int,
    val checked: Boolean,
    val quantity: Int
)