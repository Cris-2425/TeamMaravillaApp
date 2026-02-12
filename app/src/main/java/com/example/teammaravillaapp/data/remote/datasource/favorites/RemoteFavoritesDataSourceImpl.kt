package com.example.teammaravillaapp.data.remote.datasource.favorites

import com.example.teammaravillaapp.data.local.prefs.user.FavoritesFileIdPrefs
import com.example.teammaravillaapp.data.remote.api.FavoritesApi
import com.example.teammaravillaapp.data.remote.api.JsonStorageApi
import com.example.teammaravillaapp.data.remote.dto.FavoritesDto
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import retrofit2.HttpException
import javax.inject.Inject
import javax.inject.Singleton
import java.io.IOException
/**
 * DataSource remoto de **favoritos** basado en almacenamiento de ficheros JSON por `fileId`.
 *
 * ### Particularidad del backend
 * El backend no crea un recurso inexistente con `POST /json/favorites/{id}`; requiere crear primero un
 * archivo dentro de la carpeta (`PUT /json/{folder}` / endpoint específico) que devuelve un `UUID` (fileId).
 *
 * Por ello, este componente:
 * 1) Resuelve un `fileId` por usuario (persistido en [FavoritesFileIdPrefs]).
 * 2) Si no existe, crea uno con [JsonStorageApi.createFileInFolder] y lo “inicializa” guardando un JSON vacío.
 * 3) Realiza lecturas/escrituras de favoritos mediante [FavoritesApi].
 *
 * ## Concurrencia
 * - [createMutex] garantiza que el flujo “crear + persistir fileId” sea **single-flight** por proceso,
 *   evitando duplicados si varias coroutines intentan crear simultáneamente.
 * - Las operaciones de IO se ejecutan vía Retrofit en hilos de red internos.
 *
 * @property favoritesApi API específica para leer/escribir favoritos por `fileId`.
 * @property jsonApi API genérica para creación de ficheros en carpeta.
 * @property favoritesFileIdPrefs Persistencia local del `fileId` por usuario.
 *
 * @see RemoteFavoritesDataSource
 * @see FavoritesDto
 */
@Singleton
class RemoteFavoritesDataSourceImpl @Inject constructor(
    private val favoritesApi: FavoritesApi,
    private val jsonApi: JsonStorageApi,
    private val favoritesFileIdPrefs: FavoritesFileIdPrefs
) : RemoteFavoritesDataSource {

    /**
     * Mutex de creación para asegurar atomicidad del “get-or-create” del `fileId`.
     */
    private val createMutex = Mutex()

    private companion object {
        /**
         * Carpeta remota usada para almacenar los ficheros de favoritos.
         */
        const val FOLDER = "favorites"
    }

    /**
     * Resuelve el `fileId` del fichero remoto de favoritos para un usuario o lo crea si no existe.
     *
     * ### Estrategia
     * - **Fast path**: si existe en [FavoritesFileIdPrefs], se devuelve inmediatamente.
     * - **Slow path**: dentro de [createMutex], se re-verifica (double-check) y se crea un fichero remoto:
     *   1) `createFileInFolder(FOLDER)` devuelve un `id` (UUID).
     *   2) Se intenta inicializar el contenido con favoritos vacíos (best-effort).
     *   3) Se persiste el `fileId` localmente.
     *
     * La inicialización del JSON se ejecuta como *best-effort* para tolerar carreras o estados del backend:
     * el `fileId` es la pieza crítica; el contenido puede escribirse después en [saveFavorites].
     *
     * ## Concurrencia
     * Thread-safe por el uso de `Mutex` + doble verificación para minimizar contención.
     *
     * @param userId Identificador estable del usuario.
     * @return `fileId` remoto asociado a ese usuario.
     *
     * @throws HttpException Si falla la creación del fichero remoto (excepto en inicialización best-effort).
     * @throws IOException Si falla la comunicación de red.
     */
    private suspend fun getOrCreateFileId(userId: String): String {
        favoritesFileIdPrefs.get(userId)?.let { return it }

        return createMutex.withLock {
            favoritesFileIdPrefs.get(userId)?.let { return@withLock it }

            val created = jsonApi.createFileInFolder(FOLDER)
            val fileId = created.id

            runCatching { favoritesApi.save(fileId, FavoritesDto(emptyList())) }

            favoritesFileIdPrefs.set(userId, fileId)
            fileId
        }
    }

    /**
     * Recupera los favoritos del usuario desde remoto.
     *
     * Si no existe `fileId` local para el usuario, se considera que no hay favoritos remotos y se devuelve vacío.
     *
     * ### Manejo de errores
     * - `404` se interpreta como “archivo inexistente” y devuelve conjunto vacío (estado inicial).
     * - Otros códigos HTTP se propagan.
     *
     * @param userId Identificador estable del usuario.
     * @return Conjunto de IDs de receta marcadas como favoritas.
     *
     * @throws HttpException Para errores HTTP distintos de 404.
     * @throws IOException Si falla la comunicación de red.
     */
    override suspend fun getFavorites(userId: String): Set<Int> {
        val fileId = favoritesFileIdPrefs.get(userId) ?: return emptySet()

        return try {
            favoritesApi.get(fileId).recipeIds.toSet()
        } catch (e: HttpException) {
            if (e.code() == 404) emptySet() else throw e
        }
    }

    /**
     * Sobrescribe los favoritos remotos del usuario con el conjunto indicado.
     *
     * Garantiza la existencia de `fileId` (creándolo si es necesario) y persiste una lista ordenada
     * para obtener escrituras deterministas.
     *
     * @param userId Identificador estable del usuario.
     * @param ids Conjunto final de IDs favoritos.
     *
     * @throws HttpException Si el servidor responde con error HTTP (4xx/5xx).
     * @throws IOException Si falla la comunicación de red.
     */
    override suspend fun saveFavorites(userId: String, ids: Set<Int>) {
        val fileId = getOrCreateFileId(userId)
        val dto = FavoritesDto(recipeIds = ids.toList().sorted())
        favoritesApi.save(fileId, dto)
    }
}