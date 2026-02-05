package com.example.teammaravillaapp.data.remote.datasource.lists

import com.example.teammaravillaapp.data.remote.mapper.toDto
import com.example.teammaravillaapp.data.remote.mapper.toSnapshot
import com.example.teammaravillaapp.model.UserListSnapshot
import com.example.teammaravillaapp.data.remote.api.ListsApi
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RemoteListsDataSourceImpl @Inject constructor(
    private val api: ListsApi
) : RemoteListsDataSource {

    private val writeMutex = Mutex()

    override suspend fun fetchAll(): List<UserListSnapshot> =
        api.getAll().map { it.toSnapshot() }

    override suspend fun overwriteAll(lists: List<UserListSnapshot>) {
        writeMutex.withLock {
            api.saveAll(lists.map { it.toDto() })
        }
    }
}