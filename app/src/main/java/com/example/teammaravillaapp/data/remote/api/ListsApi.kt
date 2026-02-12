package com.example.teammaravillaapp.data.remote.api

import com.example.teammaravillaapp.data.remote.dto.UserListDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import java.io.IOException

/**
 * Contrato Retrofit para **listas de usuario** en el backend.
 *
 * Este API expone endpoints de colección completa (bulk):
 * - Lectura de todas las listas.
 * - Escritura sobrescribiendo el conjunto completo.
 *
 * ### Por qué endpoints “bulk”
 * Simplifica sincronización tipo “fuente de verdad”, reduciendo operaciones por item
 * y evitando merges complejos a costa de enviar colecciones completas.
 *
 * ## Concurrencia
 * Métodos `suspend`: deben invocarse desde coroutines. Retrofit gestiona el IO internamente.
 *
 * @see UserListDto
 */
interface ListsApi {

    /**
     * Recupera todas las listas persistidas en backend.
     *
     * @return [Response] con una lista de [UserListDto]. Permite inspeccionar `isSuccessful` y códigos HTTP.
     *
     * @throws IOException Si falla la comunicación de red.
     */
    @GET("json/lists/all")
    suspend fun getAll(): Response<List<UserListDto>>

    /**
     * Sobrescribe el conjunto completo de listas en backend.
     *
     * @param body Colección completa que reemplaza la existente.
     * @return [Response] vacío (`Unit`) para inspección de estado HTTP.
     *
     * @throws IOException Si falla la comunicación de red.
     */
    @POST("json/lists/all")
    suspend fun saveAll(@Body body: List<UserListDto>): Response<Unit>
}