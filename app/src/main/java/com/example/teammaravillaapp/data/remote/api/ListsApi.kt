package com.example.teammaravillaapp.data.remote.api

import com.example.teammaravillaapp.data.remote.dto.UserListDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ListsApi {

    @GET("json/lists/all")
    suspend fun getAll(): List<UserListDto>

    @POST("json/lists/all")
    suspend fun saveAll(@Body body: List<UserListDto>): Map<String, String>
}