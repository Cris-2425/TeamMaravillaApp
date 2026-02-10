package com.example.teammaravillaapp.data.seed

/**
 * Construye una lista de [SeedItem] a partir del catálogo local [ProductData].
 *
 * Uso típico:
 * - Alimentar el seed remoto ([CatalogSeeder]) con los mismos ids/datos que el seed local,
 *   asegurando consistencia entre Room y backend.
 *
 * Garantías:
 * - Copia el [id] ya normalizado de cada producto del catálogo local.
 * - Mantiene el mismo [name], [category] e imagen local ([imageRes]).
 *
 * @return Lista de items lista para:
 * - upload de imágenes si procede,
 * - publicación/overwrite del catálogo remoto.
 */
fun buildSeedItemsFromProductData(): List<SeedItem> =
    ProductData.allProducts.map { p ->
        SeedItem(
            id = p.id,
            name = p.name,
            category = p.category,
            imageRes = p.imageRes
        )
    }