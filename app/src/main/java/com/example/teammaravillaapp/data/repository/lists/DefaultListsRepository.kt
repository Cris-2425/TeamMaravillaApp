package com.example.teammaravillaapp.data.repository.lists

import com.example.teammaravillaapp.core.di.ApplicationScope
import com.example.teammaravillaapp.data.local.entity.ListItemEntity
import com.example.teammaravillaapp.data.remote.datasource.lists.RemoteListsDataSource
import com.example.teammaravillaapp.data.local.repository.lists.RoomListsRepository
import com.example.teammaravillaapp.model.UserList
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DefaultListsRepository @Inject constructor(
    private val remote: RemoteListsDataSource, // 👈 interfaz, NO Impl
    private val local: RoomListsRepository,
    @ApplicationScope private val appScope: CoroutineScope
) : ListsRepository {

    private val refreshMutex = Mutex()

    @Volatile private var lastRefreshMs: Long = 0L
    private val refreshMinIntervalMs: Long = 30_000L

    override fun observeLists(): Flow<List<UserList>> =
        local.observeLists()
            .onStart { appScope.launch { refreshIfStale() } }

    override suspend fun seedIfEmpty() {
        local.seedIfEmpty()
        runCatching { refreshIfStale() }
    }

    override suspend fun add(list: UserList): String {
        val id = local.add(list)
        runCatching { pushAllToRemote() }
        return id
    }

    override suspend fun get(id: String): UserList? = local.get(id)

    override suspend fun updateProductIds(id: String, newProductIds: List<String>) {
        local.updateProductIds(id, newProductIds)
        lastRefreshMs = System.currentTimeMillis()
        runCatching { pushAllToRemote() }
    }

    // ---- ITEMS ----
    override fun observeItems(listId: String): Flow<List<ListItemEntity>> = local.observeItems(listId)
    override suspend fun getItem(listId: String, productId: String): ListItemEntity? = local.getItem(listId, productId)
    override suspend fun addItem(listId: String, productId: String) { local.addItem(listId, productId); runCatching { pushAllToRemote() } }
    override suspend fun removeItem(listId: String, productId: String) { local.removeItem(listId, productId); runCatching { pushAllToRemote() } }
    override suspend fun toggleChecked(listId: String, productId: String) { local.toggleChecked(listId, productId); runCatching { pushAllToRemote() } }
    override suspend fun setQuantity(listId: String, productId: String, quantity: Int) { local.setQuantity(listId, productId, quantity); runCatching { pushAllToRemote() } }

    // ---- MASIVAS ----
    override suspend fun setAllChecked(listId: String, checked: Boolean) { local.setAllChecked(listId, checked); runCatching { pushAllToRemote() } }
    override suspend fun clearChecked(listId: String) { local.clearChecked(listId); runCatching { pushAllToRemote() } }

    // ---- HOME PROGRESS ----
    override fun observeProgress(): Flow<Map<String, ListProgress>> = local.observeProgress()

    override suspend fun deleteById(id: String) {
        local.deleteById(id)
        lastRefreshMs = System.currentTimeMillis()
        runCatching { pushAllToRemote() }
    }

    override suspend fun rename(id: String, newName: String) {
        local.rename(id, newName)
        lastRefreshMs = System.currentTimeMillis()
        runCatching { pushAllToRemote() }
    }

    override suspend fun incQuantity(listId: String, productId: String) { local.incQuantity(listId, productId); runCatching { pushAllToRemote() } }
    override suspend fun decQuantityMin1(listId: String, productId: String) { local.decQuantityMin1(listId, productId); runCatching { pushAllToRemote() } }

    // ---- SYNC ----
    override suspend fun refreshLists(): Result<Unit> = runCatching { forceRefresh() }

    override suspend fun forceSeed(): Result<Unit> =
        runCatching {
            local.seedIfEmpty()
            lastRefreshMs = System.currentTimeMillis()
        }

    private suspend fun refreshIfStale() {
        val now = System.currentTimeMillis()
        if (now - lastRefreshMs < refreshMinIntervalMs) return

        refreshMutex.withLock {
            val nowLocked = System.currentTimeMillis()
            if (nowLocked - lastRefreshMs < refreshMinIntervalMs) return
            runCatching { forceRefresh() }
        }
    }

    private suspend fun forceRefresh() {
        val remoteLists = remote.fetchAll()       // List<UserListSnapshot>
        local.saveAllFromRemote(remoteLists)
        lastRefreshMs = System.currentTimeMillis()
    }

    private suspend fun pushAllToRemote() {
        val snapshot = local.snapshotWithItems()  // List<UserListSnapshot>
        remote.overwriteAll(snapshot)
        lastRefreshMs = System.currentTimeMillis()
    }
}