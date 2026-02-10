package com.example.teammaravillaapp.data.remote.dto

import com.example.teammaravillaapp.model.Product
import com.example.teammaravillaapp.model.ProductCategory

/**
 * DTO que representa un producto desde la API.
 *
 * Se utiliza para:
 * - Sincronización con el backend.
 * - Mapear a la capa de dominio ([Product]).
 *
 * @property id Identificador único del producto.
 * @property name Nombre del producto.
 * @property category Categoría del producto como String (opcional). Se mapea a [ProductCategory].
 * @property imageUrl URL pública de la imagen del producto (opcional).
 */
data class ProductDto(
    val id: String,
    val name: String,
    val category: String?,
    val imageUrl: String?
)