package com.example.teammaravillaapp.data.repository

import com.example.teammaravillaapp.model.Product
import com.example.teammaravillaapp.repository.ProductRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CachedProductRepository @Inject constructor(
    private val remote: RemoteProductRepository,
    private val local: LocalProductRepository
) : ProductRepository {

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    override fun observeProducts(): Flow<List<Product>> =
        local.observeProducts()
            .onStart {
                // refresh best-effort al empezar a observar
                scope.launch {
                    runCatching {
                        val remoteList = remote.getProducts()
                        local.saveProducts(remoteList)
                    }
                }
            }

    override suspend fun getProducts(): List<Product> {
        val cached = local.getProducts()
        if (cached.isNotEmpty()) {
            // refresco best-effort sin romper la UI
            runCatching {
                val remoteList = remote.getProducts()
                local.saveProducts(remoteList)
            }
            return cached
        }

        // cache vacío: toca network
        val remoteList = remote.getProducts()
        local.saveProducts(remoteList)
        return remoteList
    }

    override suspend fun saveProducts(products: List<Product>) {
        remote.saveProducts(products)
        local.saveProducts(products)
    }

    override suspend fun addProduct(product: Product) {
        remote.addProduct(product)
        local.upsert(product)
    }

    override suspend fun deleteProduct(id: String) {
        remote.deleteProduct(id)
        local.delete(id)
    }

    override suspend fun updateProduct(product: Product) {
        remote.updateProduct(product)
        local.upsert(product)
    }
}