package com.example.teammaravillaapp.data.remote.datasource.products

import com.example.teammaravillaapp.model.Product

interface RemoteProductDataSource {
    suspend fun fetchAll(): List<Product>
    suspend fun overwriteAll(products: List<Product>)
    suspend fun upsert(product: Product)
    suspend fun deleteById(id: String)
}