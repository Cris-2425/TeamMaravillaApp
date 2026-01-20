package com.example.teammaravillaapp.repository

import com.example.teammaravillaapp.model.Product


interface ProductRepository {
    suspend fun getProducts(): List<Product>
    suspend fun saveProducts(products: List<Product>)
    suspend fun addProduct(product: Product)
    suspend fun deleteProduct(id: String)
    suspend fun updateProduct(product: Product)
}