package com.example.teammaravillaapp.data.remote.datasource.users

import com.example.teammaravillaapp.data.remote.api.JsonFilesApi
import com.example.teammaravillaapp.data.remote.dto.UserRemoteDto
import com.google.gson.Gson
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RemoteUsersDataSourceImpl @Inject constructor(
    private val api: JsonFilesApi
) : RemoteUsersDataSource {

    private val gson = Gson()
    private val folder = "users"

    override suspend fun getUser(userId: String): UserRemoteDto? =
        runCatching {
            val json = api.getFile(folder, userId)
            gson.fromJson(json, UserRemoteDto::class.java)
        }.getOrNull()

    override suspend fun saveUser(user: UserRemoteDto) {
        api.saveFile(folder, user.id, user)
    }
}