package com.example.teammaravillaapp.data.remote.datasource.favorites

interface RemoteFavoritesDataSource {
    suspend fun getFavorites(userId: String): Set<Int>
    suspend fun saveFavorites(userId: String, ids: Set<Int>)
}