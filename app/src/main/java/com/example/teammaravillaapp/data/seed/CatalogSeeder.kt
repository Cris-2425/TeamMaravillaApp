package com.example.teammaravillaapp.data.seed

import com.example.teammaravillaapp.data.repository.RemoteImageRepository
import com.example.teammaravillaapp.model.Product
import com.example.teammaravillaapp.repository.ProductRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CatalogSeeder @Inject constructor(
    private val imageRepo: RemoteImageRepository,
    private val productRepo: ProductRepository
) {
    /**
     * Seed “full”:
     * 1) Sube imágenes (multipart) para los items que tengan imageRes
     * 2) Guarda la lista completa en la API (saveProducts)
     *
     * NOTA: si tu API guarda un único JSON "all", esto SOBRESCRIBE el fichero entero.
     */
    suspend fun seedAll(items: List<SeedItem>) {
        val seededProducts: List<Product> = items.map { item ->
            val url = if (item.imageRes != null) {
                imageRepo.uploadFromDrawable(
                    id = item.id,
                    drawableRes = item.imageRes
                )
                imageRepo.buildPublicUrl(item.id)
            } else {
                null
            }

            Product(
                id = item.id,
                name = item.name,
                category = item.category,
                imageUrl = url
            )
        }

        // Guarda el catálogo completo (JSON único)
        productRepo.saveProducts(seededProducts)
    }
}