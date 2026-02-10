package com.example.teammaravillaapp.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Representa un producto almacenado en la base de datos local.
 *
 * Se utiliza en la capa de datos para persistir información sobre productos.
 *
 * @property id Identificador único del producto.
 * @property name Nombre del producto.
 * @property category Categoría a la que pertenece el producto.
 * @property imageUrl URL de la imagen del producto (opcional).
 * @property imageRes Recurso drawable local de la imagen (opcional, se puede usar en fallback).
 *
 * Ejemplo de uso con DAO:
 * ```
 * @Insert(onConflict = OnConflictStrategy.REPLACE)
 * suspend fun insertProduct(product: ProductEntity)
 * ```
 */
@Entity(tableName = "products")
data class ProductEntity(
    @PrimaryKey val id: String,
    val name: String,
    val category: String,
    val imageUrl: String?,
    val imageRes: Int?
)