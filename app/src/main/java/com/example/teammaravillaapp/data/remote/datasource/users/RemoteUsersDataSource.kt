package com.example.teammaravillaapp.data.remote.datasource.users

import com.example.teammaravillaapp.data.remote.dto.UserRemoteDto

interface RemoteUsersDataSource {
    suspend fun getUser(userId: String): UserRemoteDto?
    suspend fun saveUser(user: UserRemoteDto)
}