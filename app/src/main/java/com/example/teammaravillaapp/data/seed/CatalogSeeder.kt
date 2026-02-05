package com.example.teammaravillaapp.data.seed

import com.example.teammaravillaapp.data.remote.datasource.images.RemoteImageDataSourceImpl
import com.example.teammaravillaapp.model.Product
import com.example.teammaravillaapp.data.repository.products.ProductRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CatalogSeeder @Inject constructor(
    private val imageRepo: RemoteImageDataSourceImpl,
    private val productRepo: ProductRepository
) {
    /**
     * Seed “full”:
     * 1) Sube imágenes (multipart) para los items que tengan imageRes (si no existen ya)
     * 2) Guarda la lista completa en la API (saveProducts)
     *
     * NOTA: si tu API guarda un único JSON "all", esto SOBRESCRIBE el fichero entero.
     */
    suspend fun seedAll(items: List<SeedItem>) {
        val seededProducts: List<Product> = items.map { item ->

            val url: String? = if (item.imageRes != null) {
                // Idempotencia: si ya existe en server, no la subimos
                val alreadyThere = runCatching { imageRepo.exists(item.id) }.getOrDefault(false)

                if (alreadyThere) {
                    imageRepo.buildPublicUrl(item.id)
                } else {
                    // Robustez: si falla la subida, no rompemos todo el seed
                    val uploaded = runCatching {
                        imageRepo.uploadFromDrawable(
                            id = item.id,
                            drawableRes = item.imageRes
                        )
                    }.isSuccess

                    if (uploaded) imageRepo.buildPublicUrl(item.id) else null
                }
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

        productRepo.saveProducts(seededProducts)
    }
}