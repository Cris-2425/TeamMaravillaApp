package com.example.teammaravillaapp.page.listdetail.usecase

import com.example.teammaravillaapp.R
import com.example.teammaravillaapp.ui.events.UiResult
import com.example.teammaravillaapp.data.repository.ProductRepository
import com.example.teammaravillaapp.data.seed.ProductData
import com.example.teammaravillaapp.di.IoDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import javax.inject.Inject

class RefreshCatalogUseCase @Inject constructor(
    private val productRepository: ProductRepository,
    @IoDispatcher private val io: CoroutineDispatcher
) {

    suspend fun executeBestEffort(): UiResult<Unit> = withContext(io) {
        try {
            productRepository.getProducts()
            UiResult.Success(Unit)
        } catch (t: Throwable) {
            val cached = runCatching { productRepository.observeProducts().first() }
                .getOrDefault(emptyList())

            if (cached.isNotEmpty()) {
                // Hay datos guardados: no molestamos al usuario
                return@withContext UiResult.Success(Unit)
            }

            val seeded = runCatching {
                productRepository.saveProducts(ProductData.allProducts)
            }.isSuccess

            UiResult.Error(
                messageResId = if (seeded) R.string.snackbar_catalog_seeded else R.string.snackbar_catalog_failed,
                cause = t
            )
        }
    }
}