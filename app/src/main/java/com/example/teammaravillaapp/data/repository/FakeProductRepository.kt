package com.example.teammaravillaapp.data.repository

import com.example.teammaravillaapp.model.Product
import com.example.teammaravillaapp.repository.ProductRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FakeProductRepository @Inject constructor() : ProductRepository {

    private val products = mutableListOf(
        Product(id = "manzana", name = "Manzana", category = null, imageUrl = null),
        Product(id = "pera", name = "Pera", category = null, imageUrl = null)
    )

    override suspend fun getProducts(): List<Product> =
        products.toList()

    override suspend fun saveProducts(products: List<Product>) {
        this.products.clear()
        this.products.addAll(products)
    }

    override suspend fun addProduct(product: Product) {
        if (products.any { it.id == product.id }) return
        products.add(product)
    }

    override suspend fun deleteProduct(id: String) {
        products.removeAll { it.id == id }
    }

    override suspend fun updateProduct(product: Product) {
        val index = products.indexOfFirst { it.id == product.id }
        if (index >= 0) {
            products[index] = product
        }
    }
}