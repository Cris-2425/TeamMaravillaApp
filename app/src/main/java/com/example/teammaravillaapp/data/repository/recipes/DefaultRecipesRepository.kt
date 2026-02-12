package com.example.teammaravillaapp.data.repository.recipes

import com.example.teammaravillaapp.di.ApplicationScope
import com.example.teammaravillaapp.data.local.repository.recipes.RoomRecipesRepository
import com.example.teammaravillaapp.data.remote.datasource.recipes.RemoteRecipesDataSource
import com.example.teammaravillaapp.model.IngredientLine
import com.example.teammaravillaapp.model.RecipeWithIngredients
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton
import com.example.teammaravillaapp.data.sync.mapper.toEntity

/**
 * Implementación por defecto de [RecipesRepository] como **Single Source of Truth** (Room-first).
 *
 * La UI consume exclusivamente el estado local. El backend se usa para hidratar/actualizar el caché,
 * pero fallos de red no deben degradar el funcionamiento base.
 *
 * ## Estrategia de refresco
 * - `recipes` y `observeRecipe(id)` disparan refresh perezoso (`onStart`).
 * - Se aplica **throttling** con [refreshMinIntervalMs] y serialización con [refreshMutex].
 *
 * ## Tolerancia a backend inestable
 * - `IOException` se interpreta como ausencia de conectividad → no se propaga.
 * - `404` se interpreta como “sin datos remotos” → no se propaga.
 * - Otros errores HTTP se propagan para que la capa superior pueda reportar/telemetría.
 *
 * @property remote DataSource remoto de recetas (DTOs).
 * @property local Repositorio Room (agregado de dominio).
 * @property appScope Scope de aplicación para tareas de refresh no bloqueantes.
 *
 * @see RemoteRecipesDataSource
 * @see RoomRecipesRepository
 */
@Singleton
class DefaultRecipesRepository @Inject constructor(
    private val remote: RemoteRecipesDataSource,
    private val local: RoomRecipesRepository,
    @ApplicationScope private val appScope: CoroutineScope
) : RecipesRepository {

    /**
     * Mutex para evitar refresh simultáneos.
     */
    private val refreshMutex = Mutex()

    /**
     * Marca temporal del último refresh exitoso (ms).
     */
    @Volatile
    private var lastRefreshMs: Long = 0L

    /**
     * Intervalo mínimo entre refreshes automáticos.
     */
    private val refreshMinIntervalMs: Long = 30_000L

    /**
     * Flujo principal de recetas (agregado) observado por la UI.
     *
     * Dispara un refresh perezoso en background al inicio de la observación.
     */
    override val recipes: Flow<List<RecipeWithIngredients>> =
        local.observeAll()
            .onStart { appScope.launch { refreshIfStale() } }

    /**
     * Observa una receta por ID desde local, con refresh perezoso.
     *
     * @param id ID de la receta.
     * @return `Flow` con la receta o `null`.
     */
    override fun observeRecipe(id: Int): Flow<RecipeWithIngredients?> =
        local.observeById(id)
            .onStart { appScope.launch { refreshIfStale() } }

    /**
     * Recupera una receta puntual desde local (*snapshot*).
     *
     * @param id ID de la receta.
     * @return Receta o `null`.
     */
    override suspend fun getRecipe(id: Int): RecipeWithIngredients? =
        local.getById(id)

    /**
     * Observa líneas de ingredientes (proyección) desde local.
     *
     * @param recipeId ID de la receta.
     * @return `Flow` con líneas de ingredientes.
     */
    override fun observeIngredientLines(recipeId: Int): Flow<List<IngredientLine>> =
        local.observeIngredientLines(recipeId)

    /**
     * Inserta seed local si está vacío y luego intenta refrescar desde remoto.
     *
     * La red se trata como opcional para no penalizar el arranque offline.
     */
    override suspend fun seedIfEmpty() {
        local.seedIfEmpty()
        runCatching { refreshIfStale() }.getOrNull()
    }

    /**
     * Ejecuta un refresh remoto si el último estado se considera obsoleto.
     *
     * Implementa doble comprobación para minimizar contención cuando múltiples triggers
     * (varios `onStart`) ocurren casi simultáneamente.
     */
    private suspend fun refreshIfStale() {
        val now = System.currentTimeMillis()
        if (now - lastRefreshMs < refreshMinIntervalMs) return

        refreshMutex.withLock {
            val nowLocked = System.currentTimeMillis()
            if (nowLocked - lastRefreshMs < refreshMinIntervalMs) return

            runCatching { forceRefresh() }
                .onFailure { }
        }
    }

    /**
     * Descarga recetas desde remoto y reemplaza el caché local.
     *
     * Solo actúa si hay datos remotos no vacíos para evitar sobrescribir el estado local con “nada”
     * en escenarios donde el backend aún no tiene colección inicial.
     *
     * @throws HttpException Si ocurre un error HTTP distinto de 404.
     * @throws IOException Si falla la comunicación (se intercepta y se ignora en el llamador).
     */
    private suspend fun forceRefresh() {
        val remoteDtos = try {
            remote.fetchAll()
        } catch (e: IOException) {
            return
        } catch (e: HttpException) {
            if (e.code() == 404) return
            throw e
        }

        if (remoteDtos.isEmpty()) return

        val recipes = remoteDtos.map { it.toEntity() }
        val refs = remoteDtos.flatMap { dto -> dto.ingredients.map { it.toEntity(dto.id) } }

        local.replaceAll(recipes, refs)
        lastRefreshMs = System.currentTimeMillis()
    }
}