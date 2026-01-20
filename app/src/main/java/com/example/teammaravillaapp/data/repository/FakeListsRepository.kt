package com.example.teammaravillaapp.data.repository

import com.example.teammaravillaapp.data.FakeUserLists
import com.example.teammaravillaapp.model.Product
import com.example.teammaravillaapp.model.UserList
import com.example.teammaravillaapp.repository.ListsRepository
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FakeListsRepository @Inject constructor() : ListsRepository {

    override val lists: StateFlow<List<Pair<String, UserList>>> = FakeUserLists.lists

    override fun seedIfEmpty() = FakeUserLists.seedIfEmpty()

    override fun add(list: UserList): String = FakeUserLists.add(list)

    override fun get(id: String): UserList? = FakeUserLists.get(id)

    override fun updateProducts(id: String, newProducts: List<Product>) {
        FakeUserLists.updateProducts(id, newProducts)
    }
}