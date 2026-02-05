package com.example.teammaravillaapp.data.remote.datasource.lists

import com.example.teammaravillaapp.model.UserListSnapshot

interface RemoteListsDataSource {
    suspend fun fetchAll(): List<UserListSnapshot>
    suspend fun overwriteAll(lists: List<UserListSnapshot>)
}