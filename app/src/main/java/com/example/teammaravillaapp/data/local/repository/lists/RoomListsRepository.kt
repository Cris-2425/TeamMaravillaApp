package com.example.teammaravillaapp.data.local.repository.lists

import com.example.teammaravillaapp.data.local.dao.ListsDao
import com.example.teammaravillaapp.data.local.entity.ListEntity
import com.example.teammaravillaapp.data.local.entity.ListItemEntity
import com.example.teammaravillaapp.data.local.mapper.toDomain
import com.example.teammaravillaapp.data.local.mapper.toEntity
import com.example.teammaravillaapp.data.local.mapper.toSnapshot
import com.example.teammaravillaapp.data.repository.lists.ListProgress
import com.example.teammaravillaapp.model.ListBackground
import com.example.teammaravillaapp.model.UserList
import com.example.teammaravillaapp.model.UserListSnapshot
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RoomListsRepository @Inject constructor(
    private val dao: ListsDao
) {

    fun observeLists(): Flow<List<UserList>> =
        dao.observeAll().map { roomLists -> roomLists.map { it.toDomain() } }

    fun observeItems(listId: String): Flow<List<ListItemEntity>> =
        dao.observeItems(listId)

    suspend fun getItem(listId: String, productId: String): ListItemEntity? =
        dao.getItem(listId, productId)

    suspend fun get(id: String): UserList? =
        dao.getById(id)?.toDomain()

    suspend fun seedIfEmpty() {
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

    suspend fun add(list: UserList): String {
        val id = UUID.randomUUID().toString()
        val normalized = list.copy(id = id)

        val createdAt = System.currentTimeMillis()
        dao.upsert(normalized.toEntity(createdAt = createdAt))
        dao.replaceItemsPreserveMeta(id, normalized.productIds, baseTime = createdAt)
        return id
    }

    suspend fun updateProductIds(id: String, newProductIds: List<String>) {
        val current = dao.getById(id) ?: return
        dao.replaceItemsPreserveMeta(id, newProductIds, baseTime = current.list.createdAt)
    }

    suspend fun deleteById(id: String) {
        dao.deleteItemsForList(id)
        dao.deleteById(id)
    }

    suspend fun rename(id: String, newName: String) {
        val current = dao.getById(id) ?: return
        dao.upsert(current.list.copy(name = newName))
    }

    suspend fun addItem(listId: String, productId: String) {
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

    suspend fun removeItem(listId: String, productId: String) {
        val removedPosition = dao.getItemPosition(listId, productId) ?: return
        dao.deleteItem(listId, productId)
        dao.shiftPositionsAfter(listId, removedPosition)
    }

    suspend fun toggleChecked(listId: String, productId: String) =
        dao.toggleChecked(listId, productId)

    suspend fun setQuantity(listId: String, productId: String, quantity: Int) =
        dao.setQuantity(listId, productId, quantity.coerceAtLeast(1))

    suspend fun setAllChecked(listId: String, checked: Boolean) = dao.setAllChecked(listId, checked)

    suspend fun clearChecked(listId: String) {
        dao.deleteChecked(listId)
        val remaining = dao.getItems(listId)
            .sortedBy { it.position }
            .mapIndexed { idx, it -> it.copy(position = idx) }

        dao.deleteItemsForList(listId)
        if (remaining.isNotEmpty()) dao.upsertItems(remaining)
    }

    fun observeProgress(): Flow<Map<String, ListProgress>> =
        dao.observeProgress().map { rows ->
            rows.associate { r -> r.listId to ListProgress(r.checkedCount, r.totalCount) }
        }

    suspend fun incQuantity(listId: String, productId: String) = dao.incQuantity(listId, productId)
    suspend fun decQuantityMin1(listId: String, productId: String) =
        dao.decQuantityMin1(listId, productId)

    // --------- NUEVO: sync helpers ---------

    suspend fun snapshotWithItems(): List<UserListSnapshot> =
        dao.getAllSnapshot().map { it.toSnapshot() }

    /**
     * Guardado masivo desde remoto.
     * Como tu Domain no trae meta, reconstruimos los items manteniendo meta local si ya existía.
     */
    suspend fun saveAllFromRemote(remote: List<UserListSnapshot>) {
        // Necesitas estos dos métodos en DAO:
        // clearItems() y clearLists()
        dao.clearItems()
        dao.clearLists()

        remote.forEach { snap ->
            dao.upsert(
                ListEntity(
                    id = snap.id,
                    name = snap.name,
                    background = snap.background.name,
                    createdAt = snap.createdAt
                )
            )
        }

        val items = remote.flatMap { snap ->
            snap.items.map { it ->
                ListItemEntity(
                    listId = snap.id,
                    productId = it.productId,
                    addedAt = it.addedAt,
                    position = it.position,
                    checked = it.checked,
                    quantity = it.quantity
                )
            }
        }

        if (items.isNotEmpty()) dao.upsertItems(items)
    }
}