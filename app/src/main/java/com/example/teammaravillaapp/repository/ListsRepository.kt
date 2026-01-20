package com.example.teammaravillaapp.repository

import com.example.teammaravillaapp.model.Product
import com.example.teammaravillaapp.model.UserList
import kotlinx.coroutines.flow.StateFlow

interface ListsRepository {
    val lists: StateFlow<List<Pair<String, UserList>>>

    fun seedIfEmpty()
    fun add(list: UserList): String
    fun get(id: String): UserList?
    fun updateProducts(id: String, newProducts: List<Product>)
}