package com.example.teammaravillaapp.data.repository

import com.example.teammaravillaapp.data.local.entity.ListItemEntity
import com.example.teammaravillaapp.model.UserList
import kotlinx.coroutines.flow.Flow

interface ListsRepository {

    val lists: Flow<List<Pair<String, UserList>>>

    suspend fun seedIfEmpty()

    suspend fun add(list: UserList): String

    suspend fun get(id: String): UserList?

    /**
     * Para merges masivos (ej: ingredientes de receta).
     * IMPORTANTE: preserva checked/quantity.
     */
    suspend fun updateProductIds(id: String, newProductIds: List<String>)

    // ---- ITEMS ----

    fun observeItems(listId: String): Flow<List<ListItemEntity>>

    suspend fun addItem(listId: String, productId: String)

    suspend fun removeItem(listId: String, productId: String)

    suspend fun toggleChecked(listId: String, productId: String)

    suspend fun setQuantity(listId: String, productId: String, quantity: Int)

    // ---- MASIVAS ----

    suspend fun setAllChecked(listId: String, checked: Boolean)

    suspend fun clearChecked(listId: String)

    // ---- HOME PROGRESS ----

    fun observeProgress(): Flow<Map<String, ListProgress>>
    suspend fun deleteById(id: String) {
    }

    suspend fun rename(id: String, newName: String)
}

/**
 * Progreso agregado por lista para Home.
 */
data class ListProgress(
    val checkedCount: Int,
    val totalCount: Int
) {
    val percent: Float
        get() = if (totalCount <= 0) 0f else checkedCount.toFloat() / totalCount.toFloat()
}