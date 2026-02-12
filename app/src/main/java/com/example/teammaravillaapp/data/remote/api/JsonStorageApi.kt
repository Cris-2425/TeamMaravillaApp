package com.example.teammaravillaapp.data.remote.api

import com.example.teammaravillaapp.data.remote.dto.CreateResponseDto
import com.google.gson.JsonElement
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import java.io.IOException
import retrofit2.HttpException

/**
 * API genérica para almacenamiento de **ficheros JSON** en el backend.
 *
 * Expone un *blob store* JSON organizado por `folder` e `id`. Se utiliza como primitiva
 * para funcionalidades que necesitan persistencia remota sin un modelo REST tradicional.
 *
 * ## Contrato y tipado
 * - `getFile` devuelve `JsonElement` para no acoplar la capa de datos a un esquema concreto.
 * - `postFile` acepta `Any` para permitir reutilización con distintos DTOs; la validación/shape
 *   depende del backend.
 *
 * ## Concurrencia
 * Métodos `suspend`: deben invocarse desde coroutines. Retrofit gestiona el IO internamente.
 *
 * @see CreateResponseDto
 */
interface JsonStorageApi {

    /**
     * Recupera un fichero JSON por `folder` e `id`.
     *
     * @param folder Carpeta lógica (namespace) del recurso.
     * @param id Identificador del fichero dentro de la carpeta.
     * @return Contenido del fichero como [JsonElement].
     *
     * @throws HttpException Si el servidor responde con error HTTP (4xx/5xx).
     * @throws IOException Si falla la comunicación de red.
     */
    @GET("json/{folder}/{id}")
    suspend fun getFile(
        @Path("folder") folder: String,
        @Path("id") id: String
    ): JsonElement

    /**
     * Crea o sobrescribe un fichero JSON por `folder` e `id`.
     *
     * @param folder Carpeta lógica (namespace) del recurso.
     * @param id Identificador del fichero dentro de la carpeta.
     * @param body Objeto serializable por Gson/Moshi (según configuración de Retrofit).
     * @return Mapa de metadatos devuelto por el backend (p. ej. mensajes/IDs).
     *
     * @throws HttpException Si el servidor responde con error HTTP (4xx/5xx).
     * @throws IOException Si falla la comunicación de red.
     */
    @POST("json/{folder}/{id}")
    suspend fun postFile(
        @Path("folder") folder: String,
        @Path("id") id: String,
        @Body body: Any
    ): Map<String, String>

    /**
     * Crea la carpeta si no existe.
     *
     * Diseñado como operación **idempotente** en el backend: múltiples llamadas no deben fallar ni
     * duplicar recursos.
     *
     * @param folder Carpeta lógica a crear/asegurar.
     * @param body Cuerpo opcional (por compatibilidad del endpoint). Por defecto se envía un mapa vacío.
     * @return DTO con la respuesta de creación.
     *
     * @throws HttpException Si el servidor responde con error HTTP (4xx/5xx).
     * @throws IOException Si falla la comunicación de red.
     */
    @PUT("json/{folder}")
    suspend fun createFileInFolder(
        @Path("folder") folder: String,
        @Body body: Any = emptyMap<String, Any>()
    ): CreateResponseDto
}