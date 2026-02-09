package com.example.teammaravillaapp.page.listdetail.usecase

import com.example.teammaravillaapp.R
import com.example.teammaravillaapp.data.repository.products.ProductRepository
import com.example.teammaravillaapp.di.IoDispatcher
import com.example.teammaravillaapp.ui.events.UiResult
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * Caso de uso para refrescar el catálogo de productos con estrategia “best effort”.
 *
 * Objetivo:
 * - Intentar traer datos desde API (vía [ProductRepository.refreshProducts]).
 * - Si falla, no molestar al usuario si ya existe caché local en Room.
 * - Si no hay caché, intentar un seed local forzado como plan B.
 *
 * Esto mejora UX:
 * - Evita spamear errores si el usuario ya tiene datos.
 * - Garantiza que la pantalla no quede “vacía” en primera ejecución sin red.
 *
 * @property productRepository Repositorio de productos/catálogo.
 * @property io Dispatcher de IO para ejecutar operaciones de red/DB fuera del main thread.
 *
 * Ejemplo de uso:
 * {@code
 * val result = refreshCatalogUseCase.executeBestEffort()
 * if (result is UiResult.Error) emitEventSnackbar(result.messageResId)
 * }
 */
class RefreshCatalogUseCase @Inject constructor(
    private val productRepository: ProductRepository,
    @IoDispatcher private val io: CoroutineDispatcher
) {

    /**
     * Ejecuta el refresco del catálogo “lo mejor posible” sin degradar la experiencia.
     *
     * @return [UiResult.Success] si:
     * - el refresh remoto funciona, o
     * - falla pero existe caché en Room.
     *
     * Devuelve [UiResult.Error] si:
     * - falla el refresh remoto, y
     * - no hay caché local, y
     * - el seed forzado no consigue datos suficientes (o no se puede garantizar éxito).
     *
     * @throws Exception No suele lanzar: se captura la consulta a Room con runCatching,
     * pero puede propagar si el repositorio lanza excepciones no encapsuladas.
     */
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