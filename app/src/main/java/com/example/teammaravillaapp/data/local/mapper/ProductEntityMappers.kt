package com.example.teammaravillaapp.data.local.mapper

import com.example.teammaravillaapp.data.local.entity.ProductEntity
import com.example.teammaravillaapp.model.Product
import com.example.teammaravillaapp.model.ProductCategory

fun ProductEntity.toDomain(): Product =
    Product(
        id = id,
        name = name,
        imageRes = imageRes,
        category = runCatching { ProductCategory.valueOf(category) }
            .getOrElse { ProductCategory.OTHER },
        imageUrl = imageUrl
    )

fun Product.toEntity(): ProductEntity =
    ProductEntity(
        id = id,
        name = name,
        category = category.name,
        imageUrl = imageUrl,
        imageRes = imageRes
    )