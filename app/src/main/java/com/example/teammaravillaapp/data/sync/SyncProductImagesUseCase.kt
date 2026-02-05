package com.example.teammaravillaapp.data.sync

import com.example.teammaravillaapp.data.repository.products.ProductRepository
import com.example.teammaravillaapp.data.remote.datasource.images.RemoteImageDataSourceImpl
import com.example.teammaravillaapp.data.remote.datasource.products.RemoteProductDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SyncProductImagesUseCase @Inject constructor(
    private val repo: ProductRepository,
    private val remoteProducts: RemoteProductDataSource,
    private val images: RemoteImageDataSourceImpl
) {
    suspend fun execute(): Result<Int> = runCatching {
        withContext(Dispatchers.IO) {

            // 1) fuente: lo que tengas en Room (repo.observeProducts ya está alimentado por Room)
            val localProducts = repo.getLocalProducts()
            if (localProducts.isEmpty()) return@withContext 0

            // 2) sube imágenes faltantes
            localProducts.forEach { p ->
                val res = p.imageRes ?: return@forEach
                val id = p.id
                if (!images.exists(id)) {
                    images.uploadFromDrawable(id = id, drawableRes = res)
                }
            }

            // 3) setea imageUrl deseada
            val updated = localProducts.map { p ->
                val desiredUrl = if (p.imageRes != null) images.buildPublicUrl(p.id) else p.imageUrl
                if (p.imageUrl != desiredUrl) p.copy(imageUrl = desiredUrl) else p
            }

            val changedCount = updated.count { u ->
                val old = localProducts.firstOrNull { it.id == u.id }
                old?.imageUrl != u.imageUrl
            }

            if (changedCount > 0) {
                remoteProducts.overwriteAll(updated) // POST all
                repo.refreshProducts()               // vuelve a bajar a Room
            }

            changedCount
        }
    }
}