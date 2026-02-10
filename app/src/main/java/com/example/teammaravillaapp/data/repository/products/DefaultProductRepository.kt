package com.example.teammaravillaapp.data.repository.products

import com.example.teammaravillaapp.core.di.ApplicationScope
import com.example.teammaravillaapp.data.remote.datasource.products.RemoteProductDataSourceImpl
import com.example.teammaravillaapp.data.local.repository.products.RoomProductRepository
import com.example.teammaravillaapp.data.seed.ProductData
import com.example.teammaravillaapp.model.Product
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DefaultProductRepository @Inject constructor(
    private val remote: RemoteProductDataSourceImpl,
    private val local: RoomProductRepository,
    @ApplicationScope private val appScope: CoroutineScope
) : ProductRepository {

    private val refreshMutex = Mutex()

    @Volatile
    private var lastRefreshMs: Long = 0L

    private val refreshMinIntervalMs: Long = 30_000L

    override fun observeProducts(): Flow<List<Product>> =
        local.observeProducts()
            .onStart {
                // refresh best-effort (no bloquea UI)
                appScope.launch { refreshIfStale() }
            }

    override suspend fun getLocalProducts(): List<Product> =
        local.getProducts()

    override suspend fun refreshProducts(): Result<Unit> =
        runCatching { forceRefresh() }

    override suspend fun upsert(product: Product) {
        remote.upsert(product)
        local.upsert(product)
        lastRefreshMs = System.currentTimeMillis()
    }

    override suspend fun deleteById(id: String) {
        remote.deleteById(id)
        local.delete(id)
        lastRefreshMs = System.currentTimeMillis()
    }

    override suspend fun saveProducts(products: List<Product>) {
        remote.overwriteAll(products)
        local.saveProducts(products)
        lastRefreshMs = System.currentTimeMillis()
    }

    override suspend fun seedIfEmpty() {
        local.seedIfEmpty()
        runCatching { refreshIfStale() }
        // Lanzar sync de imágenes best-effort (si quieres)
        // appScope.launch { syncProductImagesUseCase.execute() }
    }

    private suspend fun refreshIfStale() {
        val now = System.currentTimeMillis()
        if (now - lastRefreshMs < refreshMinIntervalMs) return

        refreshMutex.withLock {
            val nowLocked = System.currentTimeMillis()
            if (nowLocked - lastRefreshMs < refreshMinIntervalMs) return

            runCatching { forceRefresh() }
                .onFailure {
                    // backoff: evita reintentos constantes si no hay backend
                    lastRefreshMs = System.currentTimeMillis()
                }
        }
    }

    private suspend fun forceRefresh() {
        val remoteList = remote.fetchAll()

        // Productos actuales de Room (pueden tener imageRes del seed)
        val localById = local.getProducts().associateBy { it.id }

        val merged = remoteList.map { r ->
            val l = localById[r.id]
            r.copy(
                imageRes = r.imageRes ?: l?.imageRes,
                imageUrl = r.imageUrl ?: l?.imageUrl,
                // opcional: si tu backend no manda category bien, conserva la local
                category = if (r.category == com.example.teammaravillaapp.model.ProductCategory.OTHER)
                    (l?.category ?: r.category)
                else r.category
            )
        }

        local.saveProducts(merged)
        lastRefreshMs = System.currentTimeMillis()
    }

    override suspend fun forceSeed(): Result<Unit> = runCatching {
        val seed = ProductData.allProducts
        local.saveProducts(seed)
        lastRefreshMs = System.currentTimeMillis()
    }
}