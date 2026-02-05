package com.example.teammaravillaapp.data.local.repository.recipes

import com.example.teammaravillaapp.data.local.dao.RecipesDao
import com.example.teammaravillaapp.data.local.entity.RecipeEntity
import com.example.teammaravillaapp.data.local.entity.RecipeIngredientsCrossRef
import com.example.teammaravillaapp.data.local.mapper.toDomain
import com.example.teammaravillaapp.data.remote.mapper.toDomain
import com.example.teammaravillaapp.data.repository.recipes.RecipesRepository
import com.example.teammaravillaapp.data.seed.RecipeData
import com.example.teammaravillaapp.model.IngredientLine
import com.example.teammaravillaapp.model.RecipeWithIngredients
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

    override fun observeIngredientLines(recipeId: Int): Flow<List<IngredientLine>> =
        dao.observeIngredientLines(recipeId)
            .map { lines -> lines.map { it.toDomain() } }

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

        val ingredientRows = RecipeData.recipes.flatMap { r ->
            r.productIds.distinct().mapIndexed { index, pid ->
                RecipeIngredientsCrossRef(
                    recipeId = r.id,
                    productId = pid,
                    quantity = null,
                    unit = null,
                    position = index
                )
            }
        }

        dao.upsertRecipes(entities)
        dao.upsertCrossRefs(ingredientRows)
    }
}