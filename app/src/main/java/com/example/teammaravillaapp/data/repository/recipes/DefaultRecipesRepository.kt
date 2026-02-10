package com.example.teammaravillaapp.data.repository.recipes

import com.example.teammaravillaapp.di.ApplicationScope
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

/**
 * Implementación por defecto de [RecipesRepository].
 *
 * Actúa como **Single Source of Truth** para las recetas,
 * combinando almacenamiento local (Room) con sincronización remota.
 *
 * ### Estrategia general
 * - **Room-first**: la UI siempre observa datos locales.
 * - **Sync lazy**: el refresco remoto ocurre en background y solo si es necesario.
 * - **Protección de concurrencia**: evita múltiples refresh simultáneos.
 *
 * ### Características clave
 * - Observación reactiva con `Flow`.
 * - Seed inicial si la base está vacía.
 * - Sincronización remota bidireccional (pull + push).
 * - Throttling de refresh para proteger red y batería.
 */
@Singleton
class DefaultRecipesRepository @Inject constructor(
    private val dao: RecipesDao,
    private val remote: RemoteRecipesDataSource,
    @ApplicationScope private val appScope: CoroutineScope
) : RecipesRepository {

    /**
     * Mutex para garantizar que solo haya un refresh remoto activo a la vez.
     */
    private val refreshMutex = Mutex()

    /**
     * Marca temporal del último refresh remoto exitoso.
     */
    @Volatile
    private var lastRefreshMs: Long = 0L

    /**
     * Intervalo mínimo entre refreshes remotos.
     *
     * Evita sincronizaciones excesivas cuando múltiples observers
     * se suscriben casi simultáneamente.
     */
    private val refreshMinIntervalMs: Long = 30_000L

    /**
     * Flujo reactivo con todas las recetas y sus ingredientes.
     *
     * - Emite datos locales inmediatamente.
     * - Dispara un refresh remoto en background al comenzar la observación.
     */
    override val recipes: Flow<List<RecipeWithIngredients>> =
        dao.observeAll()
            .map { list -> list.map { it.toDomain() } }
            .onStart {
                appScope.launch { refreshIfStale() }
            }

    /**
     * Observa una receta específica por ID.
     *
     * El refresco remoto se ejecuta de forma lazy al iniciar la observación.
     */
    override fun observeRecipe(id: Int): Flow<RecipeWithIngredients?> =
        dao.observeById(id)
            .map { it?.toDomain() }
            .onStart {
                appScope.launch { refreshIfStale() }
            }

    /**
     * Obtiene una receta puntual sin observación reactiva.
     *
     * No dispara sincronización remota.
     */
    override suspend fun getRecipe(id: Int): RecipeWithIngredients? =
        dao.getById(id)?.toDomain()

    /**
     * Observa únicamente las líneas de ingredientes de una receta.
     *
     * Ideal para pantallas centradas en ingredientes o cantidades.
     */
    override fun observeIngredientLines(recipeId: Int): Flow<List<IngredientLine>> =
        dao.observeIngredientLines(recipeId)
            .map { lines -> lines.map { it.toDomain() } }

    /**
     * Inicializa la base de datos con datos seed si está vacía.
     *
     * ### Flujo
     * 1. Si hay datos → intenta refresh remoto y termina.
     * 2. Si está vacía:
     *    - Inserta recetas seed localmente.
     *    - Inserta relaciones receta–ingrediente.
     *    - Empuja los datos al backend (best-effort).
     *
     * No sobrescribe datos existentes.
     */
    override suspend fun seedIfEmpty() {
        if (dao.count() > 0) {
            runCatching { refreshIfStale() }
            return
        }

        // Local
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

        // Push a remoto
        runCatching { pushAllToRemote() }
        lastRefreshMs = System.currentTimeMillis()
    }

    /**
     * Ejecuta un refresh remoto solo si el último es considerado obsoleto.
     *
     * Protegido por mutex para evitar condiciones de carrera.
     */
    private suspend fun refreshIfStale() {
        val now = System.currentTimeMillis()
        if (now - lastRefreshMs < refreshMinIntervalMs) return

        refreshMutex.withLock {
            val nowLocked = System.currentTimeMillis()
            if (nowLocked - lastRefreshMs < refreshMinIntervalMs) return
            runCatching { forceRefresh() }
        }
    }

    /**
     * Fuerza un refresh remoto sin validar intervalo.
     *
     * - Descarga todas las recetas del backend.
     * - Reemplaza completamente el estado local.
     */
    private suspend fun forceRefresh() {
        val remoteDtos = remote.fetchAll()

        val recipes = remoteDtos.map { it.toEntity() }
        val refs = remoteDtos.flatMap { dto ->
            dto.ingredients.map { it.toEntity(dto.id) }
        }

        dao.replaceAll(recipes, refs)
        lastRefreshMs = System.currentTimeMillis()
    }

    /**
     * Empuja todo el estado local al backend.
     *
     * Usado principalmente tras un seed inicial.
     */
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