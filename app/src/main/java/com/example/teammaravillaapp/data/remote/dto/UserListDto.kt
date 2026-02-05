package com.example.teammaravillaapp.data.remote.dto

data class UserListDto(
    val id: String,
    val name: String,
    val background: String,
    val createdAt: Long,
    val items: List<ListItemDto>
)