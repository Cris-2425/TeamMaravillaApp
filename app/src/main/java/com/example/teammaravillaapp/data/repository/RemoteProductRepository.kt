package com.example.teammaravillaapp.data.repository

import com.example.teammaravillaapp.model.Product
import com.example.teammaravillaapp.network.api.ProductApi
import com.example.teammaravillaapp.network.mapper.toDomain
import com.example.teammaravillaapp.network.mapper.toDto
import com.example.teammaravillaapp.repository.ProductRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RemoteProductRepository @Inject constructor(
    private val api: ProductApi
) : ProductRepository {

    override suspend fun getProducts(): List<Product> =
        api.getAll().map { it.toDomain() }

    override suspend fun saveProducts(products: List<Product>) {
        api.saveAll(products.map { it.toDto() })
    }

    override suspend fun addProduct(product: Product) {
        val current = getProducts().toMutableList()

        val idx = current.indexOfFirst { it.id == product.id }
        if (idx >= 0) current[idx] = product else current.add(product)

        saveProducts(current)
    }

    override suspend fun deleteProduct(id: String) {
        val current = getProducts()
        val updated = current.filterNot { it.id == id }
        saveProducts(updated)
    }

    override suspend fun updateProduct(product: Product) {
        val current = getProducts().toMutableList()
        val next = current.map { if (it.id == product.id) product else it }
        saveProducts(next)
    }

    private fun mimeFor(ext: String): String = when (ext.lowercase()) {
        "jpg", "jpeg" -> "image/jpeg"
        "png" -> "image/png"
        else -> "application/octet-stream"
    }
}