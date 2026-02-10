package com.example.teammaravillaapp.page.listdetail.usecase

import com.example.teammaravillaapp.data.repository.lists.ListsRepository
import com.example.teammaravillaapp.di.IoDispatcher
import com.example.teammaravillaapp.page.listdetail.usecase.ListDetailAction
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * Caso de uso encargado de ejecutar acciones del usuario sobre el detalle de una lista.
 *
 * Este use case actúa como “puente” entre la UI/ViewModel y el [ListsRepository]:
 * recibe una [ListDetailAction] y la traduce a la operación de dominio correspondiente
 * (añadir producto, eliminar, marcar, modificar cantidad, etc.).
 *
 * Importante:
 * - Se ejecuta en el dispatcher de IO para asegurar que las operaciones de persistencia
 *   no bloquean el hilo principal.
 * - Acciones puramente de UI (por ejemplo cambios de query) pueden existir en el sealed action.
 *   En ese caso, aquí se ignoran (no tocan repositorio).
 *
 * @property listsRepository Repositorio de listas (fuente de verdad para items/estado).
 * @property io Dispatcher de IO inyectado (Hilt).
 *
 * Ejemplo de uso:
 * {@code
 * viewModelScope.launch {
 *   handleActionUseCase.execute(
 *     listId = uiState.listId!!,
 *     action = ListDetailAction.AddProduct(productId)
 *   )
 * }
 * }
 */
class ListDetailHandleActionUseCase @Inject constructor(
    private val listsRepository: ListsRepository,
    @IoDispatcher private val io: CoroutineDispatcher
) {

    /**
     * Ejecuta una acción del detalle de lista contra el repositorio.
     *
     * @param listId Identificador de la lista sobre la que se actúa. Debe ser válido y no vacío.
     * @param action Acción a aplicar (por ejemplo: añadir producto, eliminar, modificar cantidad).
     *
     * @throws Exception Puede propagar excepciones técnicas del repositorio (IO/DB/red)
     * si el repositorio no las encapsula internamente.
     *
     * Ejemplo:
     * {@code
     * handleActionUseCase.execute("list123", ListDetailAction.ToggleChecked("prod42"))
     * }
     */
    suspend fun execute(listId: String, action: ListDetailAction) = withContext(io) {
        when (action) {
            is ListDetailAction.AddProduct ->
                listsRepository.addItem(listId, action.productId)

            is ListDetailAction.RemoveProduct ->
                listsRepository.removeItem(listId, action.productId)

            is ListDetailAction.ToggleChecked ->
                listsRepository.toggleChecked(listId, action.productId)

            is ListDetailAction.IncQuantity ->
                listsRepository.incQuantity(listId, action.productId)

            is ListDetailAction.DecQuantity ->
                listsRepository.decQuantityMin1(listId, action.productId)

            ListDetailAction.RemoveChecked ->
                listsRepository.clearChecked(listId)

            ListDetailAction.UncheckAll ->
                listsRepository.setAllChecked(listId, checked = false)

            ListDetailAction.ClearList ->
                listsRepository.updateProductIds(listId, emptyList())

            // Esto supongo que no va aquí o si, pero era por refactorizar completo ya
            is ListDetailAction.QueryChanged -> Unit
        }
    }
}