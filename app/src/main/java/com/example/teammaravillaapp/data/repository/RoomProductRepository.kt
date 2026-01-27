package com.example.teammaravillaapp.data.repository

import com.example.teammaravillaapp.data.local.dao.ProductDao
import com.example.teammaravillaapp.data.local.mapper.toDomain
import com.example.teammaravillaapp.data.local.mapper.toEntity
import com.example.teammaravillaapp.data.seed.ProductData
import com.example.teammaravillaapp.model.Product
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

    suspend fun saveProducts(products: List<Product>) {
        dao.upsertAll(products.map { it.toEntity() })
    }

    suspend fun upsert(product: Product) {
        dao.upsert(product.toEntity())
    }

    suspend fun delete(id: String) {
        dao.deleteById(id)
    }

    suspend fun clear() {
        dao.clear()
    }

    suspend fun seedIfEmpty() {
        if (dao.count() > 0) return
        saveProducts(ProductData.allProducts)
    }
}