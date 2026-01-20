package com.example.teammaravillaapp.network.api

import okhttp3.MultipartBody
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query

/**
 * Endpoint multipart para subir una imagen.
 *
 * 🔧 Cambia "images" por la ruta real que indique Swagger.
 * Ejemplos típicos:
 * - "images"
 * - "api/images"
 * - "api/images/upload"
 * - "files/upload"
 */
interface ImageApi {

    /**
     * Devuelve algo como:
     * { "url": "...", "fileName": "...", ... }
     * (lo adaptamos a Map para que compile siempre).
     */
    @Multipart
    @POST("images")
    suspend fun uploadImage(
        @Query("id") id: String,
        @Part file: MultipartBody.Part
    ): Map<String, Any>
}