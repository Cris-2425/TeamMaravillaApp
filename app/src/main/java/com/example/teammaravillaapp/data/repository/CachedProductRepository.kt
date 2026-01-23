package com.example.teammaravillaapp.data.repository

import com.example.teammaravillaapp.model.Product
import com.example.teammaravillaapp.repository.ProductRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CachedProductRepository @Inject constructor(
    private val remote: RemoteProductRepository,
    private val local: LocalProductRepository
) : ProductRepository {

    // Scope interno SOLO para refresh best-effort (no bloquea UI)
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    // Evita refresh concurrentes
    private val refreshMutex = Mutex()

    // Throttle para no hacer refresh excesivo al recolectar desde varias pantallas
    @Volatile
    private var lastRefreshMs: Long = 0L

    // Puedes ajustar este valor: 30s suele ir bien
    private val refreshMinIntervalMs: Long = 30_000L

    override fun observeProducts(): Flow<List<Product>> =
        local.observeProducts()
            .onStart {
                // refresh best-effort al empezar a observar, pero controlado (mutex + throttle)
                scope.launch { refreshIfStale() }
            }

    override suspend fun getProducts(): List<Product> {
        val cached = local.getProducts()

        if (cached.isNotEmpty()) {
            // refresco best-effort sin romper la UI (y sin multiplicarlo)
            refreshIfStale()
            return cached
        }

        // cache vacío: toca network (y guardamos)
        val remoteList = remote.getProducts()
        local.saveProducts(remoteList)

        // actualizamos marca de refresh (porque acabamos de refrescar)
        lastRefreshMs = System.currentTimeMillis()

        return remoteList
    }

    override suspend fun saveProducts(products: List<Product>) {
        remote.saveProducts(products)
        local.saveProducts(products)
        lastRefreshMs = System.currentTimeMillis()
    }

    override suspend fun addProduct(product: Product) {
        remote.addProduct(product)
        local.upsert(product)
        lastRefreshMs = System.currentTimeMillis()
    }

    override suspend fun deleteProduct(id: String) {
        remote.deleteProduct(id)
        local.delete(id)
        lastRefreshMs = System.currentTimeMillis()
    }

    override suspend fun updateProduct(product: Product) {
        remote.updateProduct(product)
        local.upsert(product)
        lastRefreshMs = System.currentTimeMillis()
    }

    /**
     * Refresh best-effort controlado:
     * - Mutex: 1 refresh a la vez
     * - Throttle: no refrescar demasiado a menudo
     */
    private suspend fun refreshIfStale() {
        val now = System.currentTimeMillis()
        if (now - lastRefreshMs < refreshMinIntervalMs) return

        refreshMutex.withLock {
            // Double-check dentro del lock
            val nowLocked = System.currentTimeMillis()
            if (nowLocked - lastRefreshMs < refreshMinIntervalMs) return

            runCatching {
                val remoteList = remote.getProducts()
                local.saveProducts(remoteList)
                lastRefreshMs = System.currentTimeMillis()
            }
        }
    }
}