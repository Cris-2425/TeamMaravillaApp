package com.example.teammaravillaapp.data.remote.api

import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.*

/**
 * API para gestión de imágenes en el servidor.
 *
 * Permite obtener imágenes existentes y subir nuevas imágenes.
 */
interface ImageApi {

    /**
     * Obtiene la imagen de un recurso por su ID.
     *
     * @param id ID del recurso.
     * @return Response que contiene el cuerpo de la imagen.
     */
    @GET("images/{id}")
    suspend fun getImage(
        @Path("id") id: String
    ): Response<ResponseBody>

    /**
     * Sube una imagen al servidor.
     *
     * @param id ID del recurso al que se asocia la imagen.
     * @param file Imagen en formato multipart.
     * @return Mapa con información adicional devuelta por el backend.
     */
    @Multipart
    @POST("images")
    suspend fun uploadImage(
        @Query("id") id: String,
        @Part file: MultipartBody.Part
    ): Map<String, Any>
}