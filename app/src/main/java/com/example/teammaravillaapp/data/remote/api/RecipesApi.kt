package com.example.teammaravillaapp.data.remote.api

import com.example.teammaravillaapp.data.remote.dto.RecipeDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import java.io.IOException

/**
 * Contrato Retrofit para **recetas** en el backend.
 *
 * Opera sobre la colección completa (bulk read/write) para soportar sincronización
 * simple y determinista: el estado remoto se considera la fuente de verdad.
 *
 * ## Concurrencia
 * Métodos `suspend`: deben invocarse desde coroutines. Retrofit gestiona el IO internamente.
 *
 * @see RecipeDto
 */
interface RecipesApi {

    /**
     * Recupera todas las recetas persistidas en backend.
     *
     * @return [Response] con la lista de [RecipeDto].
     *
     * @throws IOException Si falla la comunicación de red.
     */
    @GET("json/recipes/all")
    suspend fun getAll(): Response<List<RecipeDto>>

    /**
     * Sobrescribe la colección completa de recetas en backend.
     *
     * @param body Colección completa que reemplaza la existente.
     * @return [Response] vacío (`Unit`) para inspección de estado HTTP.
     *
     * @throws IOException Si falla la comunicación de red.
     */
    @POST("json/recipes/all")
    suspend fun saveAll(@Body body: List<RecipeDto>): Response<Unit>
}