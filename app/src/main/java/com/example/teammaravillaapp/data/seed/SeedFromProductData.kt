package com.example.teammaravillaapp.data.seed

import com.example.teammaravillaapp.model.ProductCategory

fun buildSeedItemsFromProductData(): List<SeedItem> =
    ProductData.allProducts.map { p ->
        SeedItem(
            id = p.id,
            name = p.name,
            category = p.category,
            imageRes = p.imageRes
        )
    }