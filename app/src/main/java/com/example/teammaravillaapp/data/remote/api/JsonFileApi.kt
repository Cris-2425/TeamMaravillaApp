package com.example.teammaravillaapp.data.remote.api

import com.google.gson.JsonElement
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface JsonFilesApi {

    @GET("json/{folder}/{id}")
    suspend fun getFile(
        @Path("folder") folder: String,
        @Path("id") id: String
    ): JsonElement

    @POST("json/{folder}/{id}")
    suspend fun saveFile(
        @Path("folder") folder: String,
        @Path("id") id: String,
        @Body body: Any
    ): Map<String, String>
}