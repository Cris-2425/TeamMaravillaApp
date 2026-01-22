package com.example.teammaravillaapp.data.repository

import com.example.teammaravillaapp.data.local.dao.RecipesDao
import com.example.teammaravillaapp.data.local.entity.RecipeEntity
import com.example.teammaravillaapp.data.local.entity.RecipeIngredientCrossRef
import com.example.teammaravillaapp.data.local.mapper.toDomain
import com.example.teammaravillaapp.model.RecipeData
import com.example.teammaravillaapp.model.RecipeWithIngredients
import com.example.teammaravillaapp.repository.RecipesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RoomRecipesRepository @Inject constructor(
    private val dao: RecipesDao
) : RecipesRepository {

    override val recipes: Flow<List<RecipeWithIngredients>> =
        dao.observeAll().map { list -> list.map { it.toDomain() } }

    override fun observeRecipe(id: Int): Flow<RecipeWithIngredients?> =
        dao.observeById(id).map { it?.toDomain() }

    override suspend fun getRecipe(id: Int): RecipeWithIngredients? =
        dao.getById(id)?.toDomain()

    override suspend fun seedIfEmpty() {
        if (dao.count() > 0) return

        val entities = RecipeData.recipes.map { r ->
            RecipeEntity(
                id = r.id,
                title = r.title,
                imageRes = r.imageRes,
                instructions = r.instructions
            )
        }

        val crossRefs = RecipeData.recipes.flatMap { r ->
            r.productIds.map { pid -> RecipeIngredientCrossRef(r.id, pid) }
        }

        dao.upsertRecipes(entities)
        dao.upsertCrossRefs(crossRefs)
    }
}