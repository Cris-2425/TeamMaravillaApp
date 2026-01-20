package com.example.teammaravillaapp.data.seed

import com.example.teammaravillaapp.model.ProductData

fun buildSeedItemsFromProductData(): List<SeedItem> =
    ProductData.allProducts.map { p ->
        SeedItem(
            id = p.id,
            name = p.name,
            category = p.category ?: com.example.teammaravillaapp.model.ProductCategory.OTHER,
            imageRes = p.imageRes
        )
    }