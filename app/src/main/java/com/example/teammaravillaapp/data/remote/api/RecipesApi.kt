package com.example.teammaravillaapp.data.remote.api

import com.example.teammaravillaapp.data.remote.dto.RecipeDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface RecipesApi {

    @GET("json/recipes/all")
    suspend fun getAll(): List<RecipeDto>

    @POST("json/recipes/all")
    suspend fun saveAll(@Body body: List<RecipeDto>): Map<String, String>
}