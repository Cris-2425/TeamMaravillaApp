package com.example.teammaravillaapp.data.remote.mapper

import com.example.teammaravillaapp.data.remote.dto.ProductDto
import com.example.teammaravillaapp.model.Product

/**
 * DTO (API) <-> Domain
 */

fun ProductDto.toDomain(): Product =
    Product(
        id = id,
        name = name,
        category = category.toProductCategoryOrDefault(),
        imageUrl = imageUrl,
        imageRes = null // remoto nunca trae imageRes
    )

fun Product.toDto(): ProductDto =
    ProductDto(
        id = id,
        name = name,
        category = category.apiValue(),
        imageUrl = imageUrl
    )