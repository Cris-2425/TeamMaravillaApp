package com.example.teammaravillaapp.data.repository.products

import com.example.teammaravillaapp.model.Product
import kotlinx.coroutines.flow.Flow

interface ProductRepository {

    /** Source of truth para UI (Room) */
    fun observeProducts(): Flow<List<Product>>

    /** Lectura rápida de cache (Room) */
    suspend fun getLocalProducts(): List<Product>

    /** Best-effort: remoto -> local (no rompe UI si falla) */
    suspend fun refreshProducts(): Result<Unit>

    /** CRUD (tu backend lo resuelve con GET all + POST all) */
    suspend fun upsert(product: Product)
    suspend fun deleteById(id: String)

    suspend fun saveProducts(products: List<Product>)

    suspend fun seedIfEmpty()
    suspend fun forceSeed(): Result<Unit>
}
