package com.example.teammaravillaapp.page.listdetail.usecase

import com.example.teammaravillaapp.data.repository.ListsRepository
import com.example.teammaravillaapp.di.IoDispatcher
import com.example.teammaravillaapp.page.listdetail.ListDetailAction
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ListDetailHandleActionUseCase @Inject constructor(
    private val listsRepository: ListsRepository,
    @IoDispatcher private val io: CoroutineDispatcher
) {

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

            is ListDetailAction.QueryChanged -> Unit
        }
    }
}