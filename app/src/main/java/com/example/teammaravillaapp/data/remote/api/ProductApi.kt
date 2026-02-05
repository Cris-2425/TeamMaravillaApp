package com.example.teammaravillaapp.data.remote.api

import com.example.teammaravillaapp.data.remote.dto.ProductDto
import com.google.gson.JsonElement
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ProductApi {

    /**
     * Lee data/products/all.json
     */
    @GET("json/products/all")
    suspend fun getAll(): List<ProductDto>

    @GET("json/products/all")
    suspend fun getAllRaw(): JsonElement

    /**
     * Sobrescribe data/products/all.json con la lista completa.
     * Devuelve un mensaje tipo { "message": "Archivo actualizado: products/all.json" }
     */
    @POST("json/products/all")
    suspend fun saveAll(@Body body: List<ProductDto>): Map<String, String>
}