package com.example.teammaravillaapp.data.repository

import com.example.teammaravillaapp.model.Product
import com.example.teammaravillaapp.network.api.ProductApi
import com.example.teammaravillaapp.network.mapper.toDomain
import com.example.teammaravillaapp.network.mapper.toDto
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Fuente REMOTA de productos.
 *
 * Backend actual:
 * - GET  json/products/all  -> devuelve lista completa
 * - POST json/products/all  -> sobrescribe lista completa
 *
 * Importante:
 * - Para evitar pisadas (race conditions), serializamos operaciones con Mutex.
 */
@Singleton
class RemoteProductRepository @Inject constructor(
    private val api: ProductApi
) {

    private val writeMutex = Mutex()

    /**
     * GET json/products/all
     */
    suspend fun getProducts(): List<Product> =
        api.getAll().map { it.toDomain() }

    /**
     * POST json/products/all
     * Sobrescribe el archivo completo
     */
    suspend fun saveProducts(products: List<Product>) {
        api.saveAll(products.map { it.toDto() })
    }

    /**
     * Añadir producto (seguro ante concurrencia):
     * - lock
     * - GET all
     * - si ya existe id -> reemplaza (o ignora; aquí lo reemplazamos)
     * - POST all
     */
    suspend fun addProduct(product: Product) {
        writeMutex.withLock {
            val current = fetchAllDomain()
            val updated = upsertById(current, product)
            saveProducts(updated)
        }
    }

    /**
     * Eliminar producto (seguro ante concurrencia):
     * - lock
     * - GET all
     * - filtra
     * - POST all
     */
    suspend fun deleteProduct(id: String) {
        writeMutex.withLock {
            val current = fetchAllDomain()
            val updated = current.filterNot { it.id == id }
            saveProducts(updated)
        }
    }

    /**
     * Actualizar producto (seguro ante concurrencia):
     * - lock
     * - GET all
     * - reemplaza si existe; si no existe, lo añade (upsert)
     * - POST all
     */
    suspend fun updateProduct(product: Product) {
        writeMutex.withLock {
            val current = fetchAllDomain()
            val updated = upsertById(current, product)
            saveProducts(updated)
        }
    }

    // -------------------------
    // Helpers
    // -------------------------

    private suspend fun fetchAllDomain(): List<Product> =
        api.getAll().map { it.toDomain() }

    private fun upsertById(list: List<Product>, item: Product): List<Product> {
        val exists = list.any { it.id == item.id }
        return if (!exists) {
            list + item
        } else {
            list.map { if (it.id == item.id) item else it }
        }
    }
}