package com.example.teammaravillaapp.data.repository

import com.example.teammaravillaapp.data.local.dao.ListsDao
import com.example.teammaravillaapp.data.local.entity.ListItemEntity
import com.example.teammaravillaapp.data.local.mapper.toDomain
import com.example.teammaravillaapp.data.local.mapper.toEntity
import com.example.teammaravillaapp.model.ListBackground
import com.example.teammaravillaapp.model.UserList
import com.example.teammaravillaapp.repository.ListProgress
import com.example.teammaravillaapp.repository.ListsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RoomListsRepository @Inject constructor(
    private val dao: ListsDao
) : ListsRepository {

    override val lists: Flow<List<Pair<String, UserList>>> =
        dao.observeAll().map { roomLists ->
            roomLists.map { it.list.id to it.toDomain() }
        }

    override suspend fun seedIfEmpty() {
        if (dao.count() > 0) return

        val id = UUID.randomUUID().toString()
        val demo = UserList(
            id = id,
            name = "Compra semanal",
            background = ListBackground.FONDO2,
            productIds = emptyList()
        )

        val createdAt = System.currentTimeMillis()
        dao.upsert(demo.toEntity(createdAt = createdAt))
        dao.replaceItemsPreserveMeta(id, demo.productIds, baseTime = createdAt)
    }

    override suspend fun add(list: UserList): String {
        val id = UUID.randomUUID().toString()
        val normalized = list.copy(id = id)

        val createdAt = System.currentTimeMillis()
        dao.upsert(normalized.toEntity(createdAt = createdAt))
        dao.replaceItemsPreserveMeta(id, normalized.productIds, baseTime = createdAt)

        return id
    }

    override suspend fun get(id: String): UserList? =
        dao.getById(id)?.toDomain()

    override suspend fun updateProductIds(id: String, newProductIds: List<String>) {
        val current = dao.getById(id) ?: return
        dao.replaceItemsPreserveMeta(id, newProductIds, baseTime = current.list.createdAt)
    }

    // ---- ITEMS ----

    override fun observeItems(listId: String): Flow<List<ListItemEntity>> =
        dao.observeItems(listId)

    override suspend fun addItem(listId: String, productId: String) {
        val existing = dao.getItems(listId)
        if (existing.any { it.productId == productId }) return

        val now = System.currentTimeMillis()
        val pos = existing.size

        dao.upsertItem(
            ListItemEntity(
                listId = listId,
                productId = productId,
                addedAt = now,
                position = pos,
                checked = false,
                quantity = 1
            )
        )
    }

    override suspend fun removeItem(listId: String, productId: String) {
        val removedPosition = dao.getItemPosition(listId, productId) ?: return
        dao.deleteItem(listId, productId)
        dao.shiftPositionsAfter(listId, removedPosition)
    }

    override suspend fun toggleChecked(listId: String, productId: String) {
        dao.toggleChecked(listId, productId)
    }

    override suspend fun setQuantity(listId: String, productId: String, quantity: Int) {
        dao.setQuantity(listId, productId, quantity.coerceAtLeast(1))
    }

    // ---- MASIVAS ----

    override suspend fun setAllChecked(listId: String, checked: Boolean) {
        dao.setAllChecked(listId, checked)
    }

    override suspend fun clearChecked(listId: String) {
        // borra comprados
        dao.deleteChecked(listId)

        // recompacta posiciones (porque hemos borrado filas intermedias)
        val remaining = dao.getItems(listId)
            .sortedBy { it.position }
            .mapIndexed { idx, it -> it.copy(position = idx) }

        dao.deleteItemsForList(listId)
        if (remaining.isNotEmpty()) dao.upsertItems(remaining)
    }

    // ---- HOME PROGRESS ----

    override fun observeProgress(): Flow<Map<String, ListProgress>> =
        dao.observeProgress()
            .map { rows ->
                rows.associate { r ->
                    r.listId to ListProgress(
                        checkedCount = r.checkedCount,
                        totalCount = r.totalCount
                    )
                }
            }
}