package com.example.teammaravillaapp.data.remote.api

import com.example.teammaravillaapp.data.remote.dto.UserListDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

/**
 * API para gestionar listas de usuario (User Lists) en el backend.
 *
 * Permite obtener y sobrescribir la colección completa de listas.
 */
interface ListsApi {

    /**
     * Obtiene todas las listas de usuario.
     *
     * Endpoint: GET /json/lists/all
     *
     * @return Lista de [UserListDto] representando todas las listas.
     */
    @GET("json/lists/all")
    suspend fun getAll(): List<UserListDto>

    /**
     * Sobrescribe todas las listas de usuario en el backend.
     *
     * Endpoint: POST /json/lists/all
     *
     * @param body Lista completa de [UserListDto] que reemplazará a la existente.
     * @return Mapa con información adicional devuelta por el backend, generalmente un mensaje de confirmación.
     */
    @POST("json/lists/all")
    suspend fun saveAll(@Body body: List<UserListDto>): Map<String, String>
}