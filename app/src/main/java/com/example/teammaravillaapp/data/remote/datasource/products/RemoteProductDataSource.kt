package com.example.teammaravillaapp.data.remote.datasource.products

import com.example.teammaravillaapp.model.Product

/**
 * Fuente de datos remota para productos.
 *
 * Define los contratos para interactuar con los productos en el backend.
 * La implementación concreta puede usar Retrofit, OkHttp u otra librería HTTP.
 */
interface RemoteProductDataSource {

    /**
     * Obtiene todos los productos desde el backend.
     *
     * @return Lista de [Product] con los datos de los productos.
     */
    suspend fun fetchAll(): List<Product>

    /**
     * Sobrescribe todos los productos en el backend con la lista proporcionada.
     *
     * Reemplaza completamente el estado remoto de productos.
     *
     * @param products Lista de [Product] a enviar al backend.
     */
    suspend fun overwriteAll(products: List<Product>)

    /**
     * Inserta o actualiza un producto en el backend.
     *
     * Si el producto ya existe (por [Product.id]), lo reemplaza.
     * Si no existe, lo agrega.
     *
     * @param product Producto a insertar o actualizar.
     */
    suspend fun upsert(product: Product)

    /**
     * Elimina un producto del backend por su [id].
     *
     * @param id Identificador del producto a eliminar.
     */
    suspend fun deleteById(id: String)
}