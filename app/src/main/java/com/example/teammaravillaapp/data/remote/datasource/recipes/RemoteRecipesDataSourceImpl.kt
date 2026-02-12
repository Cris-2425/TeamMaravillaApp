package com.example.teammaravillaapp.data.remote.datasource.recipes

import com.example.teammaravillaapp.data.remote.api.RecipesApi
import com.example.teammaravillaapp.data.remote.dto.RecipeDto
import com.example.teammaravillaapp.util.listBodyOrEmptyOnSuccess
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import retrofit2.HttpException
import javax.inject.Inject
import javax.inject.Singleton
import java.io.IOException

/**
 * DataSource remoto para **recetas** basado en endpoints “bulk” (colección completa).
 *
 * En este módulo se opera a nivel de DTO ([RecipeDto]) para mantener el DataSource como capa
 * de transporte. La conversión a dominio suele ocurrir en repositorios/mappers superiores.
 *
 * ## Concurrencia
 * [writeMutex] garantiza un único escritor sobre la colección remota, evitando condiciones de carrera
 * al sobrescribir el conjunto completo.
 *
 * @property api Cliente Retrofit para recetas.
 *
 * @see RemoteRecipesDataSource
 * @see RecipesApi
 */
@Singleton
class RemoteRecipesDataSourceImpl @Inject constructor(
    private val api: RecipesApi,
) : RemoteRecipesDataSource {

    /**
     * Mutex para serializar escrituras (single-writer).
     */
    private val writeMutex = Mutex()

    /**
     * Descarga la colección completa de recetas (DTO) sin adquirir [writeMutex].
     *
     * Separado como método interno para poder reutilizarlo dentro del mutex en operaciones
     * de escritura sin duplicar peticiones.
     *
     * ### Manejo de errores
     * - `404` se interpreta como “no hay datos” (estado inicial) y devuelve lista vacía.
     * - Otros códigos HTTP se propagan.
     *
     * @return Lista de [RecipeDto] obtenida desde remoto.
     *
     * @throws HttpException Para errores HTTP distintos de 404.
     * @throws IOException Si falla la comunicación de red.
     */
    private suspend fun fetchAllInternal(): List<RecipeDto> =
        try {
            api.getAll().listBodyOrEmptyOnSuccess()
        } catch (e: HttpException) {
            if (e.code() == 404) emptyList() else throw e
        }

    /**
     * Descarga la colección completa de recetas.
     *
     * @return Lista de [RecipeDto].
     *
     * @throws HttpException Para errores HTTP distintos de 404.
     * @throws IOException Si falla la comunicación de red.
     */
    override suspend fun fetchAll(): List<RecipeDto> = fetchAllInternal()

    /**
     * Sobrescribe la colección completa de recetas en remoto.
     *
     * Se ejecuta bajo [writeMutex] para evitar sobrescrituras concurrentes.
     *
     * @param recipes Colección final de [RecipeDto] a persistir.
     *
     * @throws HttpException Si la respuesta no es exitosa.
     * @throws IOException Si falla la comunicación de red.
     */
    override suspend fun overwriteAll(recipes: List<RecipeDto>) {
        writeMutex.withLock {
            val res = api.saveAll(recipes)
            if (!res.isSuccessful) throw HttpException(res)
        }
    }
}