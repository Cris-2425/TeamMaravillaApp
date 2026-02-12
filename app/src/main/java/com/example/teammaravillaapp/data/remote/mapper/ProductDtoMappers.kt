package com.example.teammaravillaapp.data.remote.mapper

import com.example.teammaravillaapp.data.remote.dto.ProductDto
import com.example.teammaravillaapp.model.Product
import com.example.teammaravillaapp.model.ProductCategory

/**
 * Extensiones de mapeo entre el contrato remoto de **productos** ([ProductDto]) y el modelo de dominio ([Product]).
 *
 * Mantener este mapeo en un único lugar reduce duplicación y evita que la capa de presentación
 * tenga que conocer detalles del contrato API.
 *
 * Estas funciones son **puras** y seguras para uso en cualquier hilo.
 *
 * @see ProductDto
 * @see Product
 * @see ProductCategory
 */
fun ProductDto.toDomain(): Product =
    Product(
        id = id,
        name = name,
        category = category.toProductCategoryOrDefault(),
        imageUrl = imageUrl,
        imageRes = null
    )

/**
 * Convierte un producto de dominio a DTO para API.
 *
 * ### Decisión de diseño
 * `imageRes` se excluye porque es un detalle **local** (recurso Android) no representable en remoto.
 * La categoría se serializa mediante el valor API expuesto por [ProductCategory.apiValue].
 *
 * @receiver Producto de dominio.
 * @return DTO listo para transporte.
 */
fun Product.toDto(): ProductDto =
    ProductDto(
        id = id,
        name = name,
        category = category.apiValue(),
        imageUrl = imageUrl
    )