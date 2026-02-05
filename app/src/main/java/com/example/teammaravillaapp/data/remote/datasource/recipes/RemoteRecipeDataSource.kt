package com.example.teammaravillaapp.data.remote.datasource.recipes

import com.example.teammaravillaapp.data.remote.dto.RecipeDto

interface RemoteRecipesDataSource {
    suspend fun fetchAll(): List<RecipeDto>
    suspend fun overwriteAll(recipes: List<RecipeDto>)
}