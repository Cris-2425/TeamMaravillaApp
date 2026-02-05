package com.example.teammaravillaapp.data.repository.recipes

import com.example.teammaravillaapp.core.di.ApplicationScope
import com.example.teammaravillaapp.data.local.dao.RecipesDao
import com.example.teammaravillaapp.data.local.entity.RecipeEntity
import com.example.teammaravillaapp.data.local.entity.RecipeIngredientsCrossRef
import com.example.teammaravillaapp.data.local.mapper.toDomain
import com.example.teammaravillaapp.data.remote.datasource.recipes.RemoteRecipesDataSource
import com.example.teammaravillaapp.data.remote.mapper.toDomain
import com.example.teammaravillaapp.data.seed.RecipeData
import com.example.teammaravillaapp.model.IngredientLine
import com.example.teammaravillaapp.data.sync.mapper.toDto
import com.example.teammaravillaapp.data.sync.mapper.toEntity
import com.example.teammaravillaapp.model.RecipeWithIngredients
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DefaultRecipesRepository @Inject constructor(
    private val dao: RecipesDao,
    private val remote: RemoteRecipesDataSource,
    @ApplicationScope private val appScope: CoroutineScope
) : RecipesRepository {

    private val refreshMutex = Mutex()

    @Volatile private var lastRefreshMs: Long = 0L
    private val refreshMinIntervalMs: Long = 30_000L

    override val recipes: Flow<List<RecipeWithIngredients>> =
        dao.observeAll()
            .map { list -> list.map { it.toDomain() } }
            .onStart { appScope.launch { refreshIfStale() } }

    override fun observeRecipe(id: Int): Flow<RecipeWithIngredients?> =
        dao.observeById(id)
            .map { it?.toDomain() }
            .onStart { appScope.launch { refreshIfStale() } }

    override suspend fun getRecipe(id: Int): RecipeWithIngredients? =
        dao.getById(id)?.toDomain()

    override fun observeIngredientLines(recipeId: Int): Flow<List<IngredientLine>> =
        dao.observeIngredientLines(recipeId)
            .map { lines -> lines.map { it.toDomain() } }

    override suspend fun seedIfEmpty() {
        if (dao.count() > 0) {
            runCatching { refreshIfStale() }
            return
        }

        // 1) Seed LOCAL
        val entities = RecipeData.recipes.map { r ->
            RecipeEntity(
                id = r.id,
                title = r.title,
                imageRes = r.imageRes,
                instructions = r.instructions
            )
        }

        val refs = RecipeData.recipes.flatMap { r ->
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

        dao.replaceAll(entities, refs)

        // 2) Push remoto best-effort (si tu backend está vacío, lo llenas)
        runCatching { pushAllToRemote() }
        lastRefreshMs = System.currentTimeMillis()
    }

    // -------- SYNC internals --------

    private suspend fun refreshIfStale() {
        val now = System.currentTimeMillis()
        if (now - lastRefreshMs < refreshMinIntervalMs) return

        refreshMutex.withLock {
            val nowLocked = System.currentTimeMillis()
            if (nowLocked - lastRefreshMs < refreshMinIntervalMs) return
            runCatching { forceRefresh() }
        }
    }

    private suspend fun forceRefresh() {
        val remoteDtos = remote.fetchAll()

        val recipes = remoteDtos.map { it.toEntity() }
        val refs = remoteDtos.flatMap { dto ->
            dto.ingredients.map { it.toEntity(dto.id) }
        }

        dao.replaceAll(recipes, refs)
        lastRefreshMs = System.currentTimeMillis()
    }

    private suspend fun pushAllToRemote() {
        val recipeEntities = dao.getAllRecipeEntities()
        val crossRefs = dao.getAllCrossRefs()

        val refsByRecipe = crossRefs.groupBy { it.recipeId }

        val dtos = recipeEntities.map { re ->
            val ingredientDtos =
                (refsByRecipe[re.id] ?: emptyList())
                    .sortedBy { it.position }
                    .map { it.toDto() }

            re.toDto(ingredientDtos)
        }

        remote.overwriteAll(dtos)
        lastRefreshMs = System.currentTimeMillis()
    }
}