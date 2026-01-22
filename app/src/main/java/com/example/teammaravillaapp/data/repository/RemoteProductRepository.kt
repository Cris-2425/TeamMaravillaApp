package com.example.teammaravillaapp.data.repository

import com.example.teammaravillaapp.model.Product
import com.example.teammaravillaapp.network.api.ProductApi
import com.example.teammaravillaapp.network.mapper.toDomain
import com.example.teammaravillaapp.network.mapper.toDto
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Fuente REMOTA de productos.
 *
 * - Lee/escribe el JSON completo en el backend
 * - NO es reactiva (Room es la fuente observable)
 * - CachedProductRepository decide cuándo usarla
 */
@Singleton
class RemoteProductRepository @Inject constructor(
    private val api: ProductApi
) {

    /**
     * Remote no observa: la UI observa Room.
     */
    fun observeProducts(): Flow<List<Product>> = emptyFlow()

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
     * Añadir producto:
     * - Leemos todo
     * - Añadimos
     * - Guardamos todo
     */
    suspend fun addProduct(product: Product) {
        val current = api.getAll().map { it.toDomain() }
        saveProducts(current + product)
    }

    /**
     * Eliminar producto:
     * - Leemos todo
     * - Filtramos
     * - Guardamos todo
     */
    suspend fun deleteProduct(id: String) {
        val current = api.getAll().map { it.toDomain() }
        saveProducts(current.filterNot { it.id == id })
    }

    /**
     * Actualizar producto:
     * - Leemos todo
     * - Reemplazamos
     * - Guardamos todo
     */
    suspend fun updateProduct(product: Product) {
        val current = api.getAll().map { it.toDomain() }
        saveProducts(
            current.map {
                if (it.id == product.id) product else it
            }
        )
    }
}