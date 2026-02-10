package com.example.teammaravillaapp.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.example.teammaravillaapp.data.local.entity.RecipeEntity
import com.example.teammaravillaapp.data.local.entity.RecipeIngredientsCrossRef
import com.example.teammaravillaapp.data.local.entity.RecipeWithProductsRoom
import com.example.teammaravillaapp.data.remote.dto.RecipeIngredientLineDto
import kotlinx.coroutines.flow.Flow

/**
 * DAO para la gestión de recetas y sus ingredientes.
 *
 * Permite observar recetas completas, insertar, actualizar y eliminar
 * tanto recetas como la relación con productos (ingredients cross-ref).
 */
@Dao
interface RecipesDao {

    /** Observa todas las recetas con sus ingredientes, ordenadas por título. */
    @Transaction
    @Query("SELECT * FROM recipes ORDER BY title ASC")
    fun observeAll(): Flow<List<RecipeWithProductsRoom>>

    /** Observa una receta concreta con sus ingredientes por ID (Flow reactivo). */
    @Transaction
    @Query("SELECT * FROM recipes WHERE id = :id LIMIT 1")
    fun observeById(id: Int): Flow<RecipeWithProductsRoom?>

    /** Obtiene una receta concreta con sus ingredientes por ID (snapshot). */
    @Transaction
    @Query("SELECT * FROM recipes WHERE id = :id LIMIT 1")
    suspend fun getById(id: Int): RecipeWithProductsRoom?

    /** Cantidad total de recetas almacenadas. */
    @Query("SELECT COUNT(*) FROM recipes")
    suspend fun count(): Int

    /** Inserta o reemplaza múltiples recetas. */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertRecipes(items: List<RecipeEntity>)

    /** Inserta o reemplaza relaciones receta-producto (ingredientes). */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertCrossRefs(items: List<RecipeIngredientsCrossRef>)

    /** Borra todas las relaciones receta-producto. */
    @Query("DELETE FROM recipe_ingredients")
    suspend fun clearCrossRefs()

    /** Borra todas las recetas. */
    @Query("DELETE FROM recipes")
    suspend fun clearRecipes()

    /**
     * Observa las líneas de ingredientes de una receta, incluyendo cantidad, unidad y posición.
     * Devuelve un Flow reactivo de RecipeIngredientLineDto.
     */
    @Query(
        """
        SELECT p.id AS productId,
               p.name AS name,
               p.category AS category,
               p.imageUrl AS imageUrl,
               ri.quantity AS quantity,
               ri.unit AS unit,
               ri.position AS position
        FROM recipe_ingredients ri
        JOIN products p ON p.id = ri.productId
        WHERE ri.recipeId = :recipeId
        ORDER BY ri.position ASC
        """
    )
    fun observeIngredientLines(recipeId: Int): Flow<List<RecipeIngredientLineDto>>

    /** Obtiene todas las entidades de recetas (snapshot). */
    @Query("SELECT * FROM recipes ORDER BY title ASC")
    suspend fun getAllRecipeEntities(): List<RecipeEntity>

    /** Obtiene todas las relaciones receta-producto (snapshot). */
    @Query("SELECT * FROM recipe_ingredients ORDER BY recipeId ASC, position ASC")
    suspend fun getAllCrossRefs(): List<RecipeIngredientsCrossRef>

    /**
     * Reemplaza por completo todas las recetas y relaciones ingredientes.
     * Realiza borrado previo para evitar conflictos.
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