package com.example.teammaravillaapp.data.remote.api

import com.example.teammaravillaapp.data.remote.dto.FavoritesDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import java.io.IOException
import retrofit2.HttpException

/**
 * Contrato Retrofit para leer/escribir el fichero JSON de **favoritos** en el backend.
 *
 * Este endpoint trabaja por `fileId` (identificador de archivo), no por usuario directamente:
 * la app resuelve y persiste el `fileId` por usuario (p. ej. vía DataStore) y lo utiliza aquí.
 *
 * ## Concurrencia
 * Métodos `suspend`: deben invocarse desde coroutines. Retrofit ejecuta la llamada en un hilo
 * de IO internamente.
 *
 * @see FavoritesDto
 */
interface FavoritesApi {

    /**
     * Recupera el JSON de favoritos asociado a un `fileId`.
     *
     * @param userId Identificador de archivo (`fileId`) del recurso de favoritos.
     * @return DTO con el contenido completo de favoritos.
     *
     * @throws HttpException Si el servidor responde con error HTTP (4xx/5xx).
     * @throws IOException Si falla la comunicación de red.
     */
    @GET("json/favorites/{fileId}")
    suspend fun get(
        @Path("fileId") userId: String
    ): FavoritesDto

    /**
     * Persiste el JSON completo de favoritos en el fichero asociado a un `fileId`.
     *
     * Semántica de “sobrescritura”: el backend guarda el `body` como estado final del archivo.
     *
     * @param userId Identificador de archivo (`fileId`) del recurso de favoritos.
     * @param body DTO serializable con el contenido a almacenar.
     * @return Mapa de metadatos devuelto por el backend (p. ej. mensajes/IDs).
     *
     * @throws HttpException Si el servidor responde con error HTTP (4xx/5xx).
     * @throws IOException Si falla la comunicación de red.
     */
    @POST("json/favorites/{fileId}")
    suspend fun save(
        @Path("fileId") userId: String,
        @Body body: FavoritesDto
    ): Map<String, String>
}