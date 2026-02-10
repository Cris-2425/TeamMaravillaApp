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

/**
 * Implementación de [RecipesRepository] usando Room.
 *
 * - Observa recetas y sus ingredientes.
 * - Permite obtener recetas individuales.
 * - Inicializa la base de datos con seed si está vacía.
 */
@Singleton
class RoomRecipesRepository @Inject constructor(
    private val dao: RecipesDao
) : RecipesRepository {

    /** Flujo de todas las recetas con sus ingredientes. */
    override val recipes: Flow<List<RecipeWithIngredients>> =
        dao.observeAll().map { list -> list.map { it.toDomain() } }

    /** Observa una receta por su ID. */
    override fun observeRecipe(id: Int): Flow<RecipeWithIngredients?> =
        dao.observeById(id).map { it?.toDomain() }

    /** Obtiene una receta por su ID (suspend). */
    override suspend fun getRecipe(id: Int): RecipeWithIngredients? =
        dao.getById(id)?.toDomain()

    /** Observa las líneas de ingredientes de una receta. */
    override fun observeIngredientLines(recipeId: Int): Flow<List<IngredientLine>> =
        dao.observeIngredientLines(recipeId)
            .map { lines -> lines.map { it.toDomain() } }

    /**
     * Inserta datos semilla si la base de datos está vacía.
     *
     * - Inserta las recetas.
     * - Inserta los crossRefs entre receta y productos.
     */
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