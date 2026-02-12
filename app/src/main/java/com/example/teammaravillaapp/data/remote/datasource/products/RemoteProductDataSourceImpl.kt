package com.example.teammaravillaapp.data.remote.datasource.products

import com.example.teammaravillaapp.data.remote.api.ProductApi
import com.example.teammaravillaapp.data.remote.mapper.toDomain
import com.example.teammaravillaapp.data.remote.mapper.toDto
import com.example.teammaravillaapp.model.Product
import com.example.teammaravillaapp.util.listBodyOrEmptyOnSuccess
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import retrofit2.HttpException
import javax.inject.Inject
import javax.inject.Singleton
import java.io.IOException

/**
 * DataSource remoto para **productos** basado en endpoints “bulk” (colección completa).
 *
 * El backend expone lectura/escritura de la colección completa; por ello, las operaciones
 * de tipo “upsert” y “delete” se implementan como:
 * 1) descargar estado actual
 * 2) aplicar transformación en memoria
 * 3) sobrescribir colección completa
 *
 * ### Concurrencia
 * [writeMutex] serializa escrituras para evitar condiciones de carrera entre:
 * - acciones de usuario (alta/edición/borrado)
 * - sincronización automática
 *
 * Nota: [fetchAllInternal] está separada para poder ejecutarse dentro del mutex sin duplicar
 * peticiones cuando se usa como parte de operaciones de escritura.
 *
 * @property api Cliente Retrofit para productos.
 *
 * @see RemoteProductDataSource
 * @see ProductApi
 */
@Singleton
class RemoteProductDataSourceImpl @Inject constructor(
    private val api: ProductApi,
) : RemoteProductDataSource {

    /**
     * Mutex para garantizar un único escritor sobre la colección remota.
     */
    private val writeMutex = Mutex()

    /**
     * Descarga la colección completa de productos sin adquirir [writeMutex].
     *
     * Se expone como método interno para permitir composición dentro de operaciones de escritura
     * sin repetir llamadas.
     *
     * ### Manejo de errores
     * - `404` se interpreta como “no hay datos” (estado inicial) y devuelve lista vacía.
     * - Otros códigos HTTP se propagan.
     *
     * @return Lista de productos en modelo de dominio.
     *
     * @throws HttpException Para errores HTTP distintos de 404.
     * @throws IOException Si falla la comunicación de red.
     */
    private suspend fun fetchAllInternal(): List<Product> =
        try {
            api.getAll()
                .listBodyOrEmptyOnSuccess()
                .map { it.toDomain() }
        } catch (e: HttpException) {
            if (e.code() == 404) emptyList() else throw e
        }

    /**
     * Descarga la colección completa de productos.
     *
     * @return Lista de [Product] en dominio.
     *
     * @throws HttpException Para errores HTTP distintos de 404.
     * @throws IOException Si falla la comunicación de red.
     */
    override suspend fun fetchAll(): List<Product> = fetchAllInternal()

    /**
     * Sobrescribe la colección completa de productos en remoto.
     *
     * @param products Colección final a persistir.
     *
     * @throws HttpException Si la respuesta no es exitosa.
     * @throws IOException Si falla la comunicación de red.
     */
    override suspend fun overwriteAll(products: List<Product>) {
        writeMutex.withLock {
            val res = api.saveAll(products.map { it.toDto() })
            if (!res.isSuccessful) throw HttpException(res)
        }
    }

    /**
     * Inserta o actualiza un producto en remoto.
     *
     * Implementado como operación “read-modify-write” sobre la colección completa.
     * Se ejecuta bajo [writeMutex] para reducir pérdidas de actualización cuando hay
     * escrituras concurrentes.
     *
     * @param product Producto a insertar/actualizar (identificado por `id`).
     *
     * @throws HttpException Si el servidor responde con error HTTP (4xx/5xx).
     * @throws IOException Si falla la comunicación de red.
     */
    override suspend fun upsert(product: Product) {
        writeMutex.withLock {
            val current = fetchAllInternal()

            val updated =
                if (current.any { it.id == product.id }) {
                    current.map { if (it.id == product.id) product else it }
                } else {
                    current + product
                }

            api.saveAll(updated.map { it.toDto() })
        }
    }

    /**
     * Elimina un producto por ID en remoto.
     *
     * Implementado como operación “read-filter-write” sobre la colección completa.
     *
     * @param id ID del producto a eliminar.
     *
     * @throws HttpException Si el servidor responde con error HTTP (4xx/5xx).
     * @throws IOException Si falla la comunicación de red.
     */
    override suspend fun deleteById(id: String) {
        writeMutex.withLock {
            val current = fetchAllInternal()
            val updated = current.filterNot { it.id == id }
            api.saveAll(updated.map { it.toDto() })
        }
    }
}