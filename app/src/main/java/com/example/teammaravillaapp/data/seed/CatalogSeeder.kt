package com.example.teammaravillaapp.data.seed

import com.example.teammaravillaapp.data.remote.datasource.images.RemoteImageDataSourceImpl
import com.example.teammaravillaapp.data.repository.products.ProductRepository
import com.example.teammaravillaapp.model.Product
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Orquestador de seed del catálogo cuando existe backend.
 *
 * Objetivo:
 * - Publicar en el servidor un catálogo completo de [Product] partiendo de una lista de [SeedItem].
 *
 * Estrategia:
 * 1) Para cada item con [SeedItem.imageRes], intenta asegurar que su imagen está subida en el servidor:
 *    - Si `exists(id)` devuelve true, reutiliza la URL pública.
 *    - Si no existe, intenta subirla desde drawable con `uploadFromDrawable`.
 *    - Si falla la subida, el producto se guarda sin `imageUrl` (best-effort).
 * 2) Finalmente, guarda la lista completa usando [ProductRepository.saveProducts].
 *
 * Importante:
 * - Este seed es **destructivo** si tu API implementa “overwrite all”: sobrescribe el catálogo entero.
 * - Está pensado como herramienta de admin/dev, no para ejecutarse siempre en cada arranque.
 *
 * Consideraciones:
 * - El proceso es idempotente a nivel de imagen (si ya existe, no re-sube).
 * - Si el backend cae, el seed puede terminar con productos sin URL.
 *
 * @property imageRepo DataSource remoto para comprobar/subir imágenes.
 * @property productRepo Repositorio para persistir el catálogo remoto (y normalmente reflejarlo en local).
 */
@Singleton
class CatalogSeeder @Inject constructor(
    private val imageRepo: RemoteImageDataSourceImpl,
    private val productRepo: ProductRepository
) {

    /**
     * Publica un catálogo completo en backend.
     *
     * @param items Lista de items de seed (id, name, category, imageRes opcional).
     *
     * @throws Exception Puede lanzar excepciones técnicas si [productRepo.saveProducts] falla
     * (depende de tu implementación). La subida de imágenes se hace best-effort.
     */
    suspend fun seedAll(items: List<SeedItem>) {
        val seededProducts: List<Product> = items.map { item ->

            val url: String? = if (item.imageRes != null) {
                val alreadyThere = runCatching { imageRepo.exists(item.id) }
                    .getOrDefault(false)

                if (alreadyThere) {
                    imageRepo.buildPublicUrl(item.id)
                } else {
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