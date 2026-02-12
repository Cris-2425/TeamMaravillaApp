package com.example.teammaravillaapp.data.remote.datasource.lists

import com.example.teammaravillaapp.data.remote.api.ListsApi
import com.example.teammaravillaapp.data.remote.mapper.toDto
import com.example.teammaravillaapp.data.remote.mapper.toSnapshot
import com.example.teammaravillaapp.model.UserListSnapshot
import com.example.teammaravillaapp.util.listBodyOrEmptyOnSuccess
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import retrofit2.HttpException
import javax.inject.Inject
import javax.inject.Singleton
import java.io.IOException

/**
 * DataSource remoto para **listas de usuario** usando endpoints “bulk” (colección completa).
 *
 * ### Contrato remoto
 * - Lectura: obtiene todas las listas como colección.
 * - Escritura: sobrescribe la colección completa.
 *
 * ### Concurrencia
 * [writeMutex] serializa escrituras para evitar condiciones de carrera en escenarios donde
 * múltiples acciones (p. ej. auto-sync + acción de usuario) intenten sobrescribir al mismo tiempo.
 *
 * @property api Cliente Retrofit para listas.
 *
 * @see RemoteListsDataSource
 * @see ListsApi
 */
@Singleton
class RemoteListsDataSourceImpl @Inject constructor(
    private val api: ListsApi,
) : RemoteListsDataSource {

    /**
     * Mutex para serializar llamadas de sobrescritura (single-writer).
     */
    private val writeMutex = Mutex()

    /**
     * Descarga el conjunto completo de listas desde remoto.
     *
     * ### Manejo de errores
     * - `404` se interpreta como “no hay datos” (estado inicial) y devuelve lista vacía.
     * - Otros códigos HTTP se propagan.
     *
     * @return Colección de [UserListSnapshot] en modelo de dominio.
     *
     * @throws HttpException Para errores HTTP distintos de 404.
     * @throws IOException Si falla la comunicación de red.
     */
    override suspend fun fetchAll(): List<UserListSnapshot> =
        try {
            api.getAll()
                .listBodyOrEmptyOnSuccess()
                .map { it.toSnapshot() }
        } catch (e: HttpException) {
            if (e.code() == 404) emptyList() else throw e
        }

    /**
     * Sobrescribe en remoto la colección completa de listas.
     *
     * Se ejecuta bajo [writeMutex] para garantizar un único escritor, reduciendo pérdidas de actualización
     * cuando existen operaciones concurrentes.
     *
     * @param lists Colección final a persistir como estado remoto.
     *
     * @throws HttpException Si la respuesta HTTP no es exitosa.
     * @throws IOException Si falla la comunicación de red.
     */
    override suspend fun overwriteAll(lists: List<UserListSnapshot>) {
        writeMutex.withLock {
            val res = api.saveAll(lists.map { it.toDto() })
            if (!res.isSuccessful) throw HttpException(res)
        }
    }
}