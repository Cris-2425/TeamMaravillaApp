package com.example.teammaravillaapp.data.repository

import com.example.teammaravillaapp.data.local.dao.ListsDao
import com.example.teammaravillaapp.data.local.mapper.toDomain
import com.example.teammaravillaapp.data.local.mapper.toEntity
import com.example.teammaravillaapp.model.ListBackground
import com.example.teammaravillaapp.model.UserList
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
        dao.observeAll()
            .map { entities -> entities.map { it.id to it.toDomain() } }

    override suspend fun seedIfEmpty() {
        if (dao.count() > 0) return

        val demo = UserList(
            id = UUID.randomUUID().toString(),
            name = "Compra semanal",
            background = ListBackground.FONDO2,
            productIds = emptyList()
        )
        dao.upsert(demo.toEntity())
    }

    override suspend fun add(list: UserList): String {
        val id = UUID.randomUUID().toString()
        val normalized = list.copy(id = id)
        dao.upsert(normalized.toEntity())
        return id
    }

    override suspend fun get(id: String): UserList? =
        dao.getById(id)?.toDomain()

    override suspend fun updateProductIds(id: String, newProductIds: List<String>) {
        val current = dao.getById(id) ?: return
        dao.upsert(
            current.copy(productIds = newProductIds.distinct())
        )
    }
}