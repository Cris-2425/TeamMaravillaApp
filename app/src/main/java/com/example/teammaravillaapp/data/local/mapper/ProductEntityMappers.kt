package com.example.teammaravillaapp.data.local.mapper

import com.example.teammaravillaapp.data.local.entity.ProductEntity
import com.example.teammaravillaapp.model.Product
import com.example.teammaravillaapp.model.ProductCategory

/**
 * Convierte un [ProductEntity] (Room) a [Product] de dominio.
 * Maneja conversión segura del enum [ProductCategory] usando valor por defecto.
 */
fun ProductEntity.toDomain(): Product =
    Product(
        id = id,
        name = name,
        imageRes = imageRes,
        category = runCatching { ProductCategory.valueOf(category) }
            .getOrElse { ProductCategory.OTHER },
        imageUrl = imageUrl
    )

/**
 * Convierte un [Product] de dominio a [ProductEntity] para almacenamiento en Room.
 */
fun Product.toEntity(): ProductEntity =
    ProductEntity(
        id = id,
        name = name,
        category = category.name,
        imageUrl = imageUrl,
        imageRes = imageRes
    )