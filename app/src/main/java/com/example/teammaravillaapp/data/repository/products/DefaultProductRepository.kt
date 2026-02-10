package com.example.teammaravillaapp.data.repository.products

import com.example.teammaravillaapp.di.ApplicationScope
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

/**
 * Implementación por defecto de [ProductRepository].
 *
 * Repositorio **offline-first** que utiliza Room como source of truth
 * y sincroniza con el backend de forma best-effort.
 *
 * ### Estrategia de sincronización
 * - La UI observa siempre datos locales.
 * - El refresh remoto se ejecuta en background y de forma perezosa.
 * - Se evita refrescar en exceso mediante throttling + mutex.
 *
 * ### Características clave
 * - Merge inteligente entre datos remotos y locales.
 * - Protección ante backend inestable o no disponible.
 * - APIs simples y predecibles para la capa de presentación.
 */
@Singleton
class DefaultProductRepository @Inject constructor(
    private val remote: RemoteProductDataSourceImpl,
    private val local: RoomProductRepository,
    @ApplicationScope private val appScope: CoroutineScope
) : ProductRepository {

    /**
     * Mutex que garantiza un único refresh remoto concurrente.
     */
    private val refreshMutex = Mutex()

    /**
     * Marca temporal del último refresh remoto exitoso.
     */
    @Volatile
    private var lastRefreshMs: Long = 0L

    /**
     * Intervalo mínimo entre refreshes remotos.
     *
     * Evita sobrecargar red y batería cuando múltiples observers
     * se suscriben casi simultáneamente.
     */
    private val refreshMinIntervalMs: Long = 30_000L

    /**
     * Flujo reactivo de productos observado por la UI.
     *
     * - Emite inmediatamente desde Room.
     * - Dispara un refresh remoto best-effort al iniciar la observación.
     */
    override fun observeProducts(): Flow<List<Product>> =
        local.observeProducts()
            .onStart {
                // Update a remoto
                appScope.launch { refreshIfStale() }
            }

    /**
     * Obtiene los productos locales sin reactividad.
     *
     * Usado principalmente por casos de uso internos.
     */
    override suspend fun getLocalProducts(): List<Product> =
        local.getProducts()

    /**
     * Fuerza un refresh remoto hacia la base local.
     *
     * No lanza excepciones a capas superiores.
     */
    override suspend fun refreshProducts(): Result<Unit> =
        runCatching { forceRefresh() }

    /**
     * Inserta o actualiza un producto.
     *
     * La operación se replica tanto local como remotamente.
     */
    override suspend fun upsert(product: Product) {
        remote.upsert(product)
        local.upsert(product)
        lastRefreshMs = System.currentTimeMillis()
    }

    /**
     * Elimina un producto por ID tanto local como remotamente.
     */
    override suspend fun deleteById(id: String) {
        remote.deleteById(id)
        local.delete(id)
        lastRefreshMs = System.currentTimeMillis()
    }

    /**
     * Guarda una lista completa de productos.
     *
     * Usado para sincronizaciones completas o sobrescrituras.
     */
    override suspend fun saveProducts(products: List<Product>) {
        remote.overwriteAll(products)
        local.saveProducts(products)
        lastRefreshMs = System.currentTimeMillis()
    }

    /**
     * Inicializa la base local con datos seed si está vacía.
     *
     * Posteriormente intenta sincronizar con el backend.
     */
    override suspend fun seedIfEmpty() {
        local.seedIfEmpty()
        runCatching { refreshIfStale() }
    }

    /**
     * Ejecuta un refresh remoto solo si el último se considera obsoleto.
     *
     * Protegido por mutex para evitar condiciones de carrera.
     */
    private suspend fun refreshIfStale() {
        val now = System.currentTimeMillis()
        if (now - lastRefreshMs < refreshMinIntervalMs) return

        refreshMutex.withLock {
            val nowLocked = System.currentTimeMillis()
            if (nowLocked - lastRefreshMs < refreshMinIntervalMs) return

            runCatching { forceRefresh() }
                .onFailure {
                    lastRefreshMs = System.currentTimeMillis()
                }
        }
    }

    /**
     * Fuerza un refresh remoto sin validar intervalo.
     *
     * ### Estrategia de merge
     * - El backend es fuente principal.
     * - Campos locales se preservan si el backend no los provee
     *   (por ejemplo, `imageRes`, `imageUrl`, categoría).
     */
    private suspend fun forceRefresh() {
        val remoteList = remote.fetchAll()

        val localById = local.getProducts().associateBy { it.id }

        val merged = remoteList.map { remoteProduct ->
            val localProduct = localById[remoteProduct.id]

            remoteProduct.copy(
                imageRes = remoteProduct.imageRes ?: localProduct?.imageRes,
                imageUrl = remoteProduct.imageUrl ?: localProduct?.imageUrl,
                category =
                    if (remoteProduct.category == com.example.teammaravillaapp.model.ProductCategory.OTHER)
                        (localProduct?.category ?: remoteProduct.category)
                    else
                        remoteProduct.category
            )
        }

        local.saveProducts(merged)
        lastRefreshMs = System.currentTimeMillis()
    }

    /**
     * Fuerza la carga de datos seed sobrescribiendo el estado local.
     *
     * Pensado para:
     * - Debug
     * - Reset de datos
     * - Modo demo
     */
    override suspend fun forceSeed(): Result<Unit> = runCatching {
        val seed = ProductData.allProducts
        local.saveProducts(seed)
        lastRefreshMs = System.currentTimeMillis()
    }
}