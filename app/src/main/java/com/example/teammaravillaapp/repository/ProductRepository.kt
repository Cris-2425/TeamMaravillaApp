package com.example.teammaravillaapp.repository

import com.example.teammaravillaapp.model.Product
import kotlinx.coroutines.flow.Flow

interface ProductRepository {
    fun observeProducts(): Flow<List<Product>>

    suspend fun getProducts(): List<Product>
    suspend fun saveProducts(products: List<Product>)
    suspend fun addProduct(product: Product)
    suspend fun deleteProduct(id: String)
    suspend fun updateProduct(product: Product)
}