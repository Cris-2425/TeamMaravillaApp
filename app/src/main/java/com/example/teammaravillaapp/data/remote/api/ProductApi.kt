package com.example.teammaravillaapp.data.remote.api

import com.example.teammaravillaapp.data.remote.dto.ProductDto
import com.google.gson.JsonElement
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

/**
 * API para gestionar productos en el backend.
 *
 * Permite obtener y sobrescribir la lista completa de productos.
 */
interface ProductApi {

    /**
     * Obtiene todos los productos como DTO.
     *
     * Endpoint: GET /json/products/all
     *
     * @return Lista de [ProductDto] representando todos los productos.
     */
    @GET("json/products/all")
    suspend fun getAll(): List<ProductDto>

    /**
     * Obtiene todos los productos en formato crudo [JsonElement].
     *
     * Útil para inspección directa del JSON sin mapeo a DTO.
     *
     * Endpoint: GET /json/products/all
     *
     * @return JSON completo de productos como [JsonElement].
     */
    @GET("json/products/all")
    suspend fun getAllRaw(): JsonElement

    /**
     * Sobrescribe la lista completa de productos en el backend.
     *
     * Endpoint: POST /json/products/all
     *
     * @param body Lista completa de [ProductDto] que reemplazará a la existente.
     * @return Mapa con mensaje de confirmación devuelto por el backend.
     */
    @POST("json/products/all")
    suspend fun saveAll(@Body body: List<ProductDto>): Map<String, String>
}