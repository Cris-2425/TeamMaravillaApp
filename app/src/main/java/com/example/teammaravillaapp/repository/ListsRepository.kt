package com.example.teammaravillaapp.repository

import com.example.teammaravillaapp.model.UserList
import kotlinx.coroutines.flow.Flow

interface ListsRepository {

    val lists: Flow<List<Pair<String, UserList>>>

    // ✅ suspend (ya no hacemos “best effort”)
    suspend fun seedIfEmpty()

    suspend fun add(list: UserList): String

    suspend fun get(id: String): UserList?

    suspend fun updateProductIds(id: String, newProductIds: List<String>)
}