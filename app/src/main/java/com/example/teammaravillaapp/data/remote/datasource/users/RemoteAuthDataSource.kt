package com.example.teammaravillaapp.data.remote.datasource.users

import com.example.teammaravillaapp.data.remote.dto.AuthResponseDto

interface RemoteAuthDataSource {
    suspend fun login(name: String, password: String): AuthResponseDto
    suspend fun register(name: String, email: String, password: String): AuthResponseDto
}
