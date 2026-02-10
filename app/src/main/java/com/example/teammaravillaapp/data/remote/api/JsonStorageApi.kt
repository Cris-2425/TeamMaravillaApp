package com.example.teammaravillaapp.data.remote.api

import com.google.gson.JsonElement
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

/**
 * API genérica para almacenar y leer ficheros JSON en el backend.
 *
 * Estructura de la URL:
 * /json/{folder}/{id}
 *
 * - `folder`: Carpeta o categoría de los datos.
 * - `id`: Identificador único del recurso.
 */
interface JsonStorageApi {

    /**
     * Obtiene un fichero JSON de un usuario o recurso.
     *
     * @param folder Carpeta en la que se almacena el archivo.
     * @param id ID del archivo/recurso.
     * @return JSON como [JsonElement].
     */
    @GET("json/{folder}/{id}")
    suspend fun getFile(
        @Path("folder") folder: String,
        @Path("id") id: String
    ): JsonElement

    /**
     * Guarda un fichero JSON en el backend.
     *
     * @param folder Carpeta en la que se almacenará el archivo.
     * @param id ID del archivo/recurso.
     * @param body Objeto que será serializado a JSON.
     * @return Mapa con información adicional devuelta por el backend.
     */
    @POST("json/{folder}/{id}")
    suspend fun saveFile(
        @Path("folder") folder: String,
        @Path("id") id: String,
        @Body body: Any
    ): Map<String, String>
}