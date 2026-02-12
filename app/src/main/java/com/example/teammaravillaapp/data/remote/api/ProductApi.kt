package com.example.teammaravillaapp.data.remote.api

import com.example.teammaravillaapp.data.remote.dto.ProductDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import java.io.IOException

/**
 * Contrato Retrofit para **productos** en el backend.
 *
 * La sincronización se realiza en modo “bulk”: se descarga o se sobrescribe la colección completa.
 * Esto permite tratar el backend como fuente de verdad con un pipeline de sincronización simple.
 *
 * ## Concurrencia
 * Métodos `suspend`: deben invocarse desde coroutines. Retrofit gestiona el IO internamente.
 *
 * @see ProductDto
 */
interface ProductApi {

    /**
     * Recupera todos los productos persistidos en backend.
     *
     * @return [Response] con la lista de [ProductDto].
     *
     * @throws IOException Si falla la comunicación de red.
     */
    @GET("json/products/all")
    suspend fun getAll(): Response<List<ProductDto>>

    /**
     * Sobrescribe la colección completa de productos en backend.
     *
     * @param body Colección completa que reemplaza la existente.
     * @return [Response] vacío (`Unit`) para inspección de estado HTTP.
     *
     * @throws IOException Si falla la comunicación de red.
     */
    @POST("json/products/all")
    suspend fun saveAll(@Body body: List<ProductDto>): Response<Unit>
}