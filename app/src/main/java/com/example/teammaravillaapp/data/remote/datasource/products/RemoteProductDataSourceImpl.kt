package com.example.teammaravillaapp.data.remote.datasource.products

import com.example.teammaravillaapp.model.Product
import com.example.teammaravillaapp.data.remote.api.ProductApi
import com.example.teammaravillaapp.data.remote.mapper.toDomain
import com.example.teammaravillaapp.data.remote.mapper.toDto
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Implementación de [RemoteProductDataSource] usando Retrofit ([ProductApi]).
 *
 * Se encarga de:
 * - Obtener todos los productos del backend.
 * - Sobrescribir completamente los productos en el backend.
 * - Insertar o actualizar productos individuales.
 * - Eliminar productos por ID.
 *
 * Usa un [Mutex] para serializar las operaciones que modifican el estado remoto
 * y evitar condiciones de carrera.
 *
 * Anotado con [Singleton] para que Hilt provea una sola instancia.
 *
 * @property api Interfaz de Retrofit para llamadas de productos.
 */
@Singleton
class RemoteProductDataSourceImpl @Inject constructor(
    private val api: ProductApi
) : RemoteProductDataSource {

    private val writeMutex = Mutex()

    override suspend fun fetchAll(): List<Product> =
        api.getAll().map { it.toDomain() }

    override suspend fun overwriteAll(products: List<Product>) {
        api.saveAll(products.map { it.toDto() })
    }

    override suspend fun upsert(product: Product) {
        writeMutex.withLock {
            val current = fetchAll()
            val updated = if (current.any { it.id == product.id }) {
                current.map { if (it.id == product.id) product else it }
            } else {
                current + product
            }
            overwriteAll(updated)
        }
    }

    override suspend fun deleteById(id: String) {
        writeMutex.withLock {
            val current = fetchAll()
            overwriteAll(current.filterNot { it.id == id })
        }
    }
}