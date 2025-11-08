package com.example.teammaravillaapp.model

data class UserList(
    val name: String,
    val backgroundLabel: String,
    val products: List<Product> = emptyList()
)