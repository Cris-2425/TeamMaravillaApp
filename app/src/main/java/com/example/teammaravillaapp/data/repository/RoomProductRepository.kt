package com.example.teammaravillaapp.data.repository

import com.example.teammaravillaapp.data.local.dao.ProductDao
import com.example.teammaravillaapp.data.local.mapper.toDomain
import com.example.teammaravillaapp.data.local.mapper.toEntity
import com.example.teammaravillaapp.data.seed.ProductData
import com.example.teammaravillaapp.model.Product
import com.example.teammaravillaapp.util.IdNormalizer
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RoomProductRepository @Inject constructor(
    private val dao: ProductDao
) {

    fun observeProducts(): Flow<List<Product>> =
        dao.observeAll().map { list -> list.map { it.toDomain() } }

    suspend fun getProducts(): List<Product> =
        dao.getAll().map { it.toDomain() }

    /**
     * Guarda lista remota:
     * 1) Normaliza IDs (para que coincidan con el seed y con el resto de app)
     * 2) Preserva imágenes locales existentes
     * 3) Rehidrata imageRes desde ProductData si no hay imagen remota
     */
    suspend fun saveProducts(products: List<Product>) {
        val existingById = dao.getAll().associateBy { it.id } // ProductEntity por id
        val seedById: Map<String, Product> = ProductData.allProducts.associateBy { it.id }

        val normalized = products.map { incoming ->
            val normalizedId = IdNormalizer.fromName(incoming.name)

            // si backend manda un id distinto, lo forzamos
            if (incoming.id != normalizedId) {
                incoming.copy(id = normalizedId)
            } else incoming
        }

        val mergedEntities = normalized.map { incoming ->
            val old = existingById[incoming.id]
            val seed = seedById[incoming.id]

            val merged = incoming.copy(
                // URL: remoto > local > seed
                imageUrl = incoming.imageUrl ?: old?.imageUrl ?: seed?.imageUrl,

                // RES: remoto (si algún día llega) > local > seed
                imageRes = incoming.imageRes ?: old?.imageRes ?: seed?.imageRes
            )

            merged.toEntity()
        }

        dao.upsertAll(mergedEntities)
    }

    suspend fun upsert(product: Product) {
        val normalizedId = IdNormalizer.fromName(product.name)
        val normalized = if (product.id != normalizedId) product.copy(id = normalizedId) else product
        dao.upsert(normalized.toEntity())
    }

    suspend fun delete(id: String) {
        dao.deleteById(id)
    }

    suspend fun seedIfEmpty() {
        if (dao.count() > 0) return
        dao.upsertAll(ProductData.allProducts.map { it.toEntity() })
    }
}