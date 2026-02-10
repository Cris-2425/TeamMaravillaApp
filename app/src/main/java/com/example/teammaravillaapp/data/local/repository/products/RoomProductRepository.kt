package com.example.teammaravillaapp.data.local.repository.products

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

/**
 * Repositorio de productos usando Room.
 *
 * - Observa productos, obtiene la lista completa.
 * - Inserta/actualiza productos preservando imágenes locales y semilla.
 * - Normaliza IDs para consistencia.
 */
@Singleton
class RoomProductRepository @Inject constructor(
    private val dao: ProductDao
) {

    /** Flujo de todos los productos. */
    fun observeProducts(): Flow<List<Product>> =
        dao.observeAll().map { list -> list.map { it.toDomain() } }

    /** Lista completa de productos (suspend). */
    suspend fun getProducts(): List<Product> =
        dao.getAll().map { it.toDomain() }

    /**
     * Guarda lista de productos remota.
     *
     * - Normaliza IDs con [IdNormalizer].
     * - Preserva imágenes locales.
     * - Rehidrata imágenes desde el seed si es necesario.
     */
    suspend fun saveProducts(products: List<Product>) {
        val existingById = dao.getAll().associateBy { it.id }
        val seedById: Map<String, Product> = ProductData.allProducts.associateBy { it.id }

        val normalized = products.map { incoming ->
            val normalizedId = IdNormalizer.fromName(incoming.name)
            if (incoming.id != normalizedId) incoming.copy(id = normalizedId) else incoming
        }

        val mergedEntities = normalized.map { incoming ->
            val old = existingById[incoming.id]
            val seed = seedById[incoming.id]

            val merged = incoming.copy(
                imageUrl = incoming.imageUrl ?: old?.imageUrl ?: seed?.imageUrl,
                imageRes = incoming.imageRes ?: old?.imageRes ?: seed?.imageRes
            )

            merged.toEntity()
        }

        dao.upsertAll(mergedEntities)
    }

    /** Inserta o actualiza un solo producto. */
    suspend fun upsert(product: Product) {
        val normalizedId = IdNormalizer.fromName(product.name)
        val normalized = if (product.id != normalizedId) product.copy(id = normalizedId) else product
        dao.upsert(normalized.toEntity())
    }

    /** Elimina un producto por ID. */
    suspend fun delete(id: String) {
        dao.deleteById(id)
    }

    /** Inserta seed de productos si la DB está vacía o rehidrata imágenes. */
    suspend fun seedIfEmpty() {
        if (dao.count() == 0) {
            dao.upsertAll(ProductData.allProducts.map { it.toEntity() })
        } else {
            rehydrateImagesFromSeed()
        }
    }

    /** Rehidrata imágenes de la semilla si no existen en la DB actual. */
    suspend fun rehydrateImagesFromSeed() {
        val seedById = ProductData.allProducts.associateBy { it.id }
        val current = dao.getAll()

        val fixed = current.map { e ->
            val seed = seedById[e.id]
            if (e.imageRes == null && seed?.imageRes != null) {
                e.copy(imageRes = seed.imageRes)
            } else e
        }

        dao.upsertAll(fixed)
    }
}