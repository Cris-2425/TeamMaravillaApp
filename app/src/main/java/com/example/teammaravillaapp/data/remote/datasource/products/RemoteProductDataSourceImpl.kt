package com.example.teammaravillaapp.data.remote.datasource.products

import com.example.teammaravillaapp.model.Product
import com.example.teammaravillaapp.data.remote.api.ProductApi
import com.example.teammaravillaapp.data.remote.mapper.toDomain
import com.example.teammaravillaapp.data.remote.mapper.toDto
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import javax.inject.Inject
import javax.inject.Singleton

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