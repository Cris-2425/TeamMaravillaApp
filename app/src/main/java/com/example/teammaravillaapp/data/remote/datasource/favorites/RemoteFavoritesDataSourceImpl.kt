package com.example.teammaravillaapp.data.remote.datasource.favorites

import com.example.teammaravillaapp.data.remote.api.JsonFilesApi
import com.example.teammaravillaapp.data.remote.dto.FavoritesDto
import com.google.gson.Gson
import javax.inject.Inject
import javax.inject.Singleton



@Singleton
class RemoteFavoritesDataSourceImpl @Inject constructor(
    private val api: JsonFilesApi
) : RemoteFavoritesDataSource {

    private val gson = Gson()
    private val folder = "favorites"

    override suspend fun getFavorites(userId: String): Set<Int> =
        runCatching {
            val json = api.getFile(folder, userId)
            gson.fromJson(json, FavoritesDto::class.java).recipeIds.toSet()
        }.getOrElse { emptySet() } // si 404 o error -> vacío

    override suspend fun saveFavorites(userId: String, ids: Set<Int>) {
        api.saveFile(folder, userId, FavoritesDto(ids.toList().sorted()))
    }
}