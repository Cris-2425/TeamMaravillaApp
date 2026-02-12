package com.example.teammaravillaapp.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.example.teammaravillaapp.data.local.entity.RecipeEntity
import com.example.teammaravillaapp.data.local.entity.RecipeIngredientLineRoom
import com.example.teammaravillaapp.data.local.entity.RecipeIngredientsCrossRef
import com.example.teammaravillaapp.data.local.entity.RecipeWithProductsRoom
import kotlinx.coroutines.flow.Flow

/**
 * Acceso a datos de **recetas** y su composición de **ingredientes**.
 *
 * Este DAO separa:
 * - La entidad principal (`recipes`)
 * - La relación receta-producto (`recipe_ingredients`) mediante tabla intermedia (**cross-ref**)
 *
 * y expone lecturas “completas” mediante consultas anotadas con `@Transaction`, evitando estados
 * inconsistentes durante la carga de relaciones.
 *
 * ## Concurrencia
 * - Los `Flow` emitidos por Room son **cold** y se invalidan automáticamente al cambiar tablas implicadas.
 * - Los métodos `suspend` se ejecutan en el *dispatcher* de Room.
 *
 * @see RecipeEntity
 * @see RecipeIngredientsCrossRef
 * @see RecipeWithProductsRoom
 */
@Dao
interface RecipesDao {

    /**
     * Observa todas las recetas con sus ingredientes, ordenadas por `title`.
     *
     * Se usa `@Transaction` para garantizar consistencia al resolver relaciones (receta + productos)
     * en una única unidad lógica de lectura.
     *
     * @return `Flow` con la lista completa de recetas y sus ingredientes.
     */
    @Transaction
    @Query("SELECT * FROM recipes ORDER BY title ASC")
    fun observeAll(): Flow<List<RecipeWithProductsRoom>>

    /**
     * Observa una receta por ID junto a sus ingredientes.
     *
     * Útil para pantallas de detalle: emite `null` si la receta no existe y vuelve a emitir ante cambios.
     *
     * @param id Identificador de la receta.
     * @return `Flow` reactivo con la receta completa o `null`.
     */
    @Transaction
    @Query("SELECT * FROM recipes WHERE id = :id LIMIT 1")
    fun observeById(id: Int): Flow<RecipeWithProductsRoom?>

    /**
     * Recupera una receta por ID junto a sus ingredientes (*snapshot*).
     *
     * @param id Identificador de la receta.
     * @return La receta completa o `null` si no existe.
     */
    @Transaction
    @Query("SELECT * FROM recipes WHERE id = :id LIMIT 1")
    suspend fun getById(id: Int): RecipeWithProductsRoom?

    /**
     * Cuenta el número total de recetas persistidas.
     *
     * @return Total de filas en `recipes`.
     */
    @Query("SELECT COUNT(*) FROM recipes")
    suspend fun count(): Int

    /**
     * Inserta o reemplaza recetas.
     *
     * Se usa **REPLACE** para que la capa de sincronización pueda tratar la BD local como caché:
     * una receta con el mismo `id` queda actualizada sin pasos explícitos de “update”.
     *
     * @param items Recetas a persistir.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertRecipes(items: List<RecipeEntity>)

    /**
     * Inserta o reemplaza relaciones receta-producto (ingredientes).
     *
     * Se usa **REPLACE** porque el orden (`position`) y las cantidades pueden cambiar entre sincronizaciones.
     *
     * @param items Relaciones receta-producto a persistir.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertCrossRefs(items: List<RecipeIngredientsCrossRef>)

    /**
     * Elimina todas las relaciones receta-producto.
     *
     * Normalmente se invoca como paso previo en reemplazos completos para evitar relaciones huérfanas
     * o colisiones de claves compuestas.
     */
    @Query("DELETE FROM recipe_ingredients")
    suspend fun clearCrossRefs()

    /**
     * Elimina todas las recetas.
     *
     * Nota: si existen relaciones dependientes, conviene borrar primero con [clearCrossRefs].
     */
    @Query("DELETE FROM recipes")
    suspend fun clearRecipes()

    /**
     * Observa las líneas de ingredientes de una receta como proyección “lista para UI”.
     *
     * Esta consulta devuelve una vista plana que combina datos del producto (`products`) con metadatos
     * de receta (`recipe_ingredients`): `quantity`, `unit` y `position`.
     *
     * ### Por qué esta proyección
     * Evita construir la lista en memoria y asegura orden estable por `position`, reduciendo lógica en la capa
     * de presentación y minimizando lecturas adicionales.
     *
     * @param recipeId ID de la receta.
     * @return `Flow` con líneas de ingredientes ordenadas por `position` ascendente.
     *
     * @see RecipeIngredientLineRoom
     */
    @Query(
        """
    SELECT p.id AS productId,
           p.name AS name,
           p.category AS category,
           p.imageUrl AS imageUrl,
           p.imageRes AS imageRes,
           ri.quantity AS quantity,
           ri.unit AS unit,
           ri.position AS position
    FROM recipe_ingredients ri
    JOIN products p ON p.id = ri.productId
    WHERE ri.recipeId = :recipeId
    ORDER BY ri.position ASC
    """
    )
    fun observeIngredientLines(recipeId: Int): Flow<List<RecipeIngredientLineRoom>>

    /**
     * Recupera todas las recetas como entidades (*snapshot*).
     *
     * Útil para exportación, debug, o sincronización que necesite entidades crudas en lugar de relaciones.
     *
     * @return Lista de [RecipeEntity] ordenada por `title`.
     */
    @Query("SELECT * FROM recipes ORDER BY title ASC")
    suspend fun getAllRecipeEntities(): List<RecipeEntity>

    /**
     * Recupera todas las relaciones receta-producto (*snapshot*).
     *
     * @return Lista de [RecipeIngredientsCrossRef] ordenada por `recipeId` y `position`.
     */
    @Query("SELECT * FROM recipe_ingredients ORDER BY recipeId ASC, position ASC")
    suspend fun getAllCrossRefs(): List<RecipeIngredientsCrossRef>

    /**
     * Reemplaza completamente recetas y relaciones de ingredientes en una única transacción.
     *
     * Estrategia:
     * 1) borrar relaciones (`recipe_ingredients`)
     * 2) borrar recetas (`recipes`)
     * 3) insertar recetas
     * 4) insertar relaciones
     *
     * ### Por qué borrar antes
     * Asegura que:
     * - No queden relaciones huérfanas.
     * - El conjunto final refleje exactamente la fuente de verdad (ej. respuesta remota).
     * - Se eviten conflictos de claves compuestas al reordenar ingredientes.
     *
     * ## Concurrencia
     * `@Transaction` garantiza atomicidad: los observadores (`Flow`) no verán un estado intermedio.
     *
     * @param recipes Recetas a persistir como estado final.
     * @param refs Relaciones receta-producto asociadas a esas recetas.
     */
    @Transaction
    suspend fun replaceAll(
        recipes: List<RecipeEntity>,
        refs: List<RecipeIngredientsCrossRef>
    ) {
        clearCrossRefs()
        clearRecipes()
        upsertRecipes(recipes)
        upsertCrossRefs(refs)
    }
}