package com.example.teammaravillaapp.data.remote.api

import com.example.teammaravillaapp.data.remote.dto.RecipeDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

/**
 * API para gestionar recetas en el backend.
 *
 * Permite obtener y sobrescribir la colección completa de recetas.
 */
interface RecipesApi {

    /**
     * Obtiene todas las recetas.
     *
     * Endpoint: GET /json/recipes/all
     *
     * @return Lista de [RecipeDto] representando todas las recetas.
     */
    @GET("json/recipes/all")
    suspend fun getAll(): List<RecipeDto>

    /**
     * Sobrescribe la lista completa de recetas en el backend.
     *
     * Endpoint: POST /json/recipes/all
     *
     * @param body Lista completa de [RecipeDto] que reemplazará a la existente.
     * @return Mapa con mensaje de confirmación devuelto por el backend.
     */
    @POST("json/recipes/all")
    suspend fun saveAll(@Body body: List<RecipeDto>): Map<String, String>
}