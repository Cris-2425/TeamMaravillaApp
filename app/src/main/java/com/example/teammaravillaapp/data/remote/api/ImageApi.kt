package com.example.teammaravillaapp.data.remote.api

import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.*

interface ImageApi {

    @GET("images/{id}")
    suspend fun getImage(
        @Path("id") id: String
    ): Response<ResponseBody>

    @Multipart
    @POST("images")
    suspend fun uploadImage(
        @Query("id") id: String,
        @Part file: MultipartBody.Part
    ): Map<String, Any>
}
