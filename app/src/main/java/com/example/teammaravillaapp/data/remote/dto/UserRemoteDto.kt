package com.example.teammaravillaapp.data.remote.dto

data class UserRemoteDto(
    val id: String,
    val email: String,
    val password: String,
    val name: String,
    val surname: String,
    val age: Int,
    val createdAt: Long
)