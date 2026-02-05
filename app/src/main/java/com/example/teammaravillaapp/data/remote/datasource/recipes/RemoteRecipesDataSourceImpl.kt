package com.example.teammaravillaapp.data.remote.datasource.recipes

import com.example.teammaravillaapp.data.remote.api.RecipesApi
import com.example.teammaravillaapp.data.remote.dto.RecipeDto
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RemoteRecipesDataSourceImpl @Inject constructor(
    private val api: RecipesApi
) : RemoteRecipesDataSource {

    private val writeMutex = Mutex()

    override suspend fun fetchAll(): List<RecipeDto> =
        api.getAll()

    override suspend fun overwriteAll(recipes: List<RecipeDto>) {
        writeMutex.withLock {
            api.saveAll(recipes)
        }
    }
}