package com.example.teammaravillaapp.page.listdetail.usecase

import com.example.teammaravillaapp.R
import com.example.teammaravillaapp.ui.events.UiResult
import com.example.teammaravillaapp.data.repository.products.ProductRepository
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

        // 1) Intentamos refrescar (Result<Unit>)
        val refresh = productRepository.refreshProducts()

        if (refresh.isSuccess) {
            return@withContext UiResult.Success(Unit)
        }

        // 2) Si falla, miramos si hay cache en Room (source of truth)
        val cached = runCatching { productRepository.observeProducts().first() }
            .getOrDefault(emptyList())

        if (cached.isNotEmpty()) {
            // Hay datos: no molestamos al usuario
            return@withContext UiResult.Success(Unit)
        }

        // 3) Si no hay cache, intentamos seed forzado local
        val seeded = productRepository.forceSeed().isSuccess

        UiResult.Error(
            messageResId = if (seeded) R.string.snackbar_catalog_seeded
            else R.string.snackbar_catalog_failed,
            cause = refresh.exceptionOrNull()
        )
    }
}