package com.example.teammaravillaapp.data.local.repository.recipes

import com.example.teammaravillaapp.data.local.dao.RecipesDao
import com.example.teammaravillaapp.data.local.entity.RecipeEntity
import com.example.teammaravillaapp.data.local.entity.RecipeIngredientsCrossRef
import com.example.teammaravillaapp.data.local.mapper.toDomain
import com.example.teammaravillaapp.data.seed.RecipeData
import com.example.teammaravillaapp.model.IngredientLine
import com.example.teammaravillaapp.model.RecipeWithIngredients
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Implementación Room del repositorio de **recetas**.
 *
 * Expone modelos de dominio y mantiene la lógica de persistencia encapsulada:
 * - Observación reactiva de recetas completas
 * - Lecturas puntuales (*snapshots*)
 * - Reemplazo completo (ideal para sincronización/seed)
 *
 * ## Concurrencia
 * - Seguro para uso concurrente: Room gestiona el dispatcher y serializa escrituras.
 * - Transformaciones `map { ... }` son **puras** y se ejecutan en el contexto del colector del `Flow`.
 *
 * @property dao DAO de recetas, inyectado por Hilt.
 *
 * @see RecipesDao
 */
@Singleton
class RoomRecipesRepository @Inject constructor(
    private val dao: RecipesDao
) {

    /**
     * Observa todas las recetas como agregado de dominio (receta + ingredientes).
     *
     * @return `Flow` que emite la lista de recetas cada vez que cambian `recipes` o sus relaciones.
     */
    fun observeAll(): Flow<List<RecipeWithIngredients>> =
        dao.observeAll().map { list -> list.map { it.toDomain() } }

    /**
     * Observa una receta por ID en forma de dominio.
     *
     * @param id ID de la receta.
     * @return `Flow` que emite la receta o `null` si no existe.
     */
    fun observeById(id: Int): Flow<RecipeWithIngredients?> =
        dao.observeById(id).map { it?.toDomain() }

    /**
     * Recupera una receta por ID (*snapshot*).
     *
     * @param id ID de la receta.
     * @return La receta o `null` si no existe.
     */
    suspend fun getById(id: Int): RecipeWithIngredients? =
        dao.getById(id)?.toDomain()

    /**
     * Observa las líneas de ingredientes (proyección) de una receta.
     *
     * Esta vía evita cargar el agregado completo cuando solo se necesita la lista para UI.
     *
     * @param recipeId ID de la receta.
     * @return `Flow` con la lista de [IngredientLine] ordenada por `position`.
     */
    fun observeIngredientLines(recipeId: Int): Flow<List<IngredientLine>> =
        dao.observeIngredientLines(recipeId).map { lines -> lines.map { it.toDomain() } }

    /**
     * Cuenta cuántas recetas existen en local.
     *
     * @return Total de recetas persistidas.
     */
    suspend fun count(): Int = dao.count()

    /**
     * Reemplaza el estado local de recetas y relaciones de ingredientes.
     *
     * @param recipes Entidades de receta.
     * @param refs Relaciones receta-producto.
     *
     * @see RecipesDao.replaceAll
     */
    suspend fun replaceAll(recipes: List<RecipeEntity>, refs: List<RecipeIngredientsCrossRef>) =
        dao.replaceAll(recipes, refs)

    /**
     * Inserta datos seed si no existe ninguna receta en local.
     *
     * ### Por qué `seedIfEmpty`
     * - Evita sobreescribir datos del usuario si ya hay contenido.
     * - Mantiene una primera experiencia usable en modo offline/demo.
     *
     * Genera:
     * - [RecipeEntity] desde [RecipeData]
     * - [RecipeIngredientsCrossRef] con `position` determinista y `distinct()` en `productIds`
     */
    suspend fun seedIfEmpty() {
        if (dao.count() > 0) return

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
    }
}