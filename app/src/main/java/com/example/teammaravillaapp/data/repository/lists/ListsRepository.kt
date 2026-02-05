package com.example.teammaravillaapp.data.repository.lists

import com.example.teammaravillaapp.data.local.entity.ListItemEntity
import com.example.teammaravillaapp.model.UserList
import kotlinx.coroutines.flow.Flow

interface ListsRepository {

    /** Source of truth para UI (Room) */
    fun observeLists(): Flow<List<UserList>>

    /** Compatibilidad: si todavía usas el val */
    val lists: Flow<List<UserList>>
        get() = observeLists()

    suspend fun seedIfEmpty()

    suspend fun add(list: UserList): String
    suspend fun get(id: String): UserList?

    /** Para merges masivos preservando checked/quantity. */
    suspend fun updateProductIds(id: String, newProductIds: List<String>)

    // ---- ITEMS ----
    fun observeItems(listId: String): Flow<List<ListItemEntity>>
    suspend fun getItem(listId: String, productId: String): ListItemEntity?
    suspend fun addItem(listId: String, productId: String)
    suspend fun removeItem(listId: String, productId: String)
    suspend fun toggleChecked(listId: String, productId: String)
    suspend fun setQuantity(listId: String, productId: String, quantity: Int)

    // ---- MASIVAS ----
    suspend fun setAllChecked(listId: String, checked: Boolean)
    suspend fun clearChecked(listId: String)

    // ---- HOME PROGRESS ----
    fun observeProgress(): Flow<Map<String, ListProgress>>

    suspend fun deleteById(id: String)
    suspend fun rename(id: String, newName: String)
    suspend fun incQuantity(listId: String, productId: String)
    suspend fun decQuantityMin1(listId: String, productId: String)

    // ---- SYNC (API) ----
    suspend fun refreshLists(): Result<Unit>
    suspend fun forceSeed(): Result<Unit>
}

data class ListProgress(
    val checkedCount: Int,
    val totalCount: Int
) {
    val percent: Float
        get() = if (totalCount <= 0) 0f else checkedCount.toFloat() / totalCount.toFloat()
}