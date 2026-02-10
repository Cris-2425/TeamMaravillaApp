package com.example.teammaravillaapp.data.repository.products

import com.example.teammaravillaapp.model.Product
import kotlinx.coroutines.flow.Flow

/**
 * Contrato del repositorio de productos.
 *
 * Define el punto único de acceso para la gestión de productos,
 * combinando almacenamiento local (Room) y sincronización remota.
 *
 * ### Estrategia de datos
 * - **Room como source of truth** para la UI.
 * - Sincronización remota en segundo plano (best-effort).
 * - La UI nunca depende directamente del estado de red.
 *
 * Este repositorio está diseñado para aplicaciones **offline-first**.
 */
interface ProductRepository {

    /**
     * Flujo reactivo de productos para la UI.
     *
     * - Emite siempre desde Room.
     * - Refleja cambios locales y sincronizaciones remotas.
     *
     * Ideal para pantallas de listado o selección de productos.
     */
    fun observeProducts(): Flow<List<Product>>

    /**
     * Obtiene los productos almacenados localmente.
     *
     * ### Uso recomendado
     * - Operaciones internas de sincronización.
     * - Casos donde no se requiere reactividad.
     *
     * @return lista de productos desde la cache local.
     */
    suspend fun getLocalProducts(): List<Product>

    /**
     * Sincroniza productos desde el backend hacia la base local.
     *
     * ### Características
     * - Operación best-effort.
     * - Un fallo remoto no rompe la UI.
     * - Actualiza Room si la operación es exitosa.
     *
     * @return [Result] indicando éxito o error de la sincronización.
     */
    suspend fun refreshProducts(): Result<Unit>

    /**
     * Inserta o actualiza un producto.
     *
     * El backend resuelve el estado final mediante
     * una estrategia de "overwrite all".
     *
     * @param product producto a insertar o actualizar.
     */
    suspend fun upsert(product: Product)

    /**
     * Elimina un producto por su identificador.
     *
     * @param id identificador único del producto.
     */
    suspend fun deleteById(id: String)

    /**
     * Guarda una lista completa de productos en la base local.
     *
     * Usado principalmente durante sincronizaciones o seeds.
     */
    suspend fun saveProducts(products: List<Product>)

    /**
     * Inicializa la base de datos con datos seed si está vacía.
     *
     * No sobrescribe información existente.
     */
    suspend fun seedIfEmpty()

    /**
     * Fuerza la carga de datos seed, sobrescribiendo el estado actual.
     *
     * ### Uso recomendado
     * - Debug
     * - Reset de datos
     * - Modo demo
     *
     * @return [Result] indicando si la operación fue exitosa.
     */
    suspend fun forceSeed(): Result<Unit>
}