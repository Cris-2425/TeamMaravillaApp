package com.example.teammaravillaapp.data.repository.products

import com.example.teammaravillaapp.di.ApplicationScope
import com.example.teammaravillaapp.data.local.repository.products.RoomProductRepository
import com.example.teammaravillaapp.data.remote.datasource.products.RemoteProductDataSource
import com.example.teammaravillaapp.data.seed.ProductData
import com.example.teammaravillaapp.model.Product
import com.example.teammaravillaapp.model.ProductCategory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import javax.inject.Inject
import javax.inject.Singleton
import retrofit2.HttpException
import java.io.IOException

/**
 * Implementación por defecto de [ProductRepository] con estrategia **offline-first**.
 *
 * Room es la **source of truth**: la UI consume siempre datos locales. Las operaciones de escritura
 * se aplican primero en local y la sincronización remota es *best-effort* para no penalizar UX.
 *
 * ## Estrategia de refresco
 * - `observeProducts()` dispara un refresh remoto perezoso (`onStart`) para hidratar el caché.
 * - Se aplica **throttling** con [refreshMinIntervalMs] y doble comprobación con [refreshMutex]
 *   para evitar rafagas de red cuando múltiples observadores se suscriben casi simultáneamente.
 *
 * ## Estrategia de merge (pull)
 * El backend se considera fuente principal, pero se preservan detalles locales cuando el remoto no
 * los provee (o los devuelve degradados), como:
 * - `imageRes` (solo local)
 * - `imageUrl` si el backend no lo envía
 * - `category` si el remoto cae en `ProductCategory.OTHER`
 *
 * ## Concurrencia
 * - [lastRefreshMs] es `@Volatile` para lecturas coherentes entre hilos.
 * - [refreshMutex] evita refresh concurrentes, reduciendo condiciones de carrera.
 *
 * @property remote DataSource remoto de productos.
 * @property local Repositorio Room para persistencia y observación local.
 * @property appScope Scope de aplicación para disparar refresh sin acoplarse a UI.
 *
 * @see RemoteProductDataSource
 * @see RoomProductRepository
 */
@Singleton
class DefaultProductRepository @Inject constructor(
    private val remote: RemoteProductDataSource,
    private val local: RoomProductRepository,
    @ApplicationScope private val appScope: CoroutineScope
) : ProductRepository {

    /**
     * Mutex que garantiza un único refresh remoto concurrente.
     */
    private val refreshMutex = Mutex()

    /**
     * Marca temporal del último refresh/push (en ms).
     *
     * `@Volatile` evita lecturas obsoletas cuando hay hilos/coroutines distintos.
     */
    @Volatile
    private var lastRefreshMs: Long = 0L

    /**
     * Intervalo mínimo entre refreshes remotos automáticos.
     *
     * Mitiga picos de red/batería cuando varios collectors empiezan casi a la vez.
     */
    private val refreshMinIntervalMs: Long = 30_000L

    /**
     * Observa productos desde Room y dispara un refresh remoto perezoso.
     *
     * @return `Flow` de productos en modelo de dominio.
     *
     * @see refreshIfStale
     */
    override fun observeProducts(): Flow<List<Product>> =
        local.observeProducts()
            .onStart {
                appScope.launch { refreshIfStale() }
            }

    /**
     * Recupera el listado local sin reactividad.
     *
     * Útil para casos de uso internos (p. ej. merge o validaciones) sin necesidad de suscripción.
     *
     * @return Lista de productos en caché local.
     */
    override suspend fun getLocalProducts(): List<Product> =
        local.getProducts()

    /**
     * Fuerza un refresh remoto hacia el caché local.
     *
     * Se devuelve como [Result] para que la capa superior decida feedback en UI.
     *
     * @return `Result.success(Unit)` si completa; `Result.failure` si ocurre error.
     */
    override suspend fun refreshProducts(): Result<Unit> =
        runCatching { forceRefresh() }

    /**
     * Inserta o actualiza un producto en local y replica en remoto (*best-effort*).
     *
     * Se actualiza [lastRefreshMs] para evitar un pull inmediato que pueda sobrescribir el cambio.
     *
     * @param product Producto a insertar/actualizar.
     */
    override suspend fun upsert(product: Product) {
        local.upsert(product)
        runCatching { remote.upsert(product) }
        lastRefreshMs = System.currentTimeMillis()
    }

    /**
     * Elimina un producto por ID en local y replica en remoto (*best-effort*).
     *
     * @param id ID del producto.
     */
    override suspend fun deleteById(id: String) {
        local.delete(id)
        runCatching { remote.deleteById(id) }
        lastRefreshMs = System.currentTimeMillis()
    }

    /**
     * Sobrescribe la colección completa en local y replica en remoto (*best-effort*).
     *
     * Útil para sincronizaciones completas o importaciones.
     *
     * @param products Colección final a persistir.
     */
    override suspend fun saveProducts(products: List<Product>) {
        local.saveProducts(products)
        runCatching { remote.overwriteAll(products) }
        lastRefreshMs = System.currentTimeMillis()
    }

    /**
     * Inserta datos seed en local si está vacío y luego intenta refrescar desde remoto.
     *
     * La red se trata como opcional para no degradar la primera ejecución offline.
     */
    override suspend fun seedIfEmpty() {
        local.seedIfEmpty()
        runCatching { refreshIfStale() }
    }

    /**
     * Ejecuta un refresh remoto si el último estado se considera obsoleto.
     *
     * Implementa doble comprobación (antes y dentro de [refreshMutex]) para minimizar contención
     * cuando hay múltiples triggers concurrentes.
     */
    private suspend fun refreshIfStale() {
        val now = System.currentTimeMillis()
        if (now - lastRefreshMs < refreshMinIntervalMs) return

        refreshMutex.withLock {
            val nowLocked = System.currentTimeMillis()
            if (nowLocked - lastRefreshMs < refreshMinIntervalMs) return
            runCatching { forceRefresh() }
        }
    }

    /**
     * Refresca el caché local desde remoto sin validar intervalo.
     *
     * ### Merge
     * Trata el remoto como fuente principal y preserva valores locales cuando el backend no aporta
     * información o cae en defaults no deseados.
     *
     * @throws HttpException Si el backend responde con error HTTP.
     * @throws IOException Si falla la comunicación de red.
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
                    if (remoteProduct.category == ProductCategory.OTHER)
                        (localProduct?.category ?: remoteProduct.category)
                    else
                        remoteProduct.category
            )
        }

        local.saveProducts(merged)
        lastRefreshMs = System.currentTimeMillis()
    }

    /**
     * Sobrescribe el estado local con el seed incluido en la app.
     *
     * Pensado para *debug/reset/demo*: no toca red y actualiza [lastRefreshMs] para estabilizar
     * el comportamiento de refresh automático inmediatamente después.
     *
     * @return `Result.success(Unit)` si completa; `Result.failure` si ocurre error.
     *
     * @see ProductData
     */
    override suspend fun forceSeed(): Result<Unit> = runCatching {
        val seed = ProductData.allProducts
        local.saveProducts(seed)
        lastRefreshMs = System.currentTimeMillis()
    }
}