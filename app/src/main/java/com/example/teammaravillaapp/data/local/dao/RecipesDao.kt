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

@Dao
interface RecipesDao {

    @Transaction
    @Query("SELECT * FROM recipes ORDER BY title ASC")
    fun observeAll(): Flow<List<RecipeWithProductsRoom>>

    @Transaction
    @Query("SELECT * FROM recipes WHERE id = :id LIMIT 1")
    fun observeById(id: Int): Flow<RecipeWithProductsRoom?>

    @Transaction
    @Query("SELECT * FROM recipes WHERE id = :id LIMIT 1")
    suspend fun getById(id: Int): RecipeWithProductsRoom?

    @Query("SELECT COUNT(*) FROM recipes")
    suspend fun count(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertRecipes(items: List<RecipeEntity>)

    // cantidad/unidad/position
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertCrossRefs(items: List<RecipeIngredientsCrossRef>)

    @Query("DELETE FROM recipe_ingredients")
    suspend fun clearCrossRefs()

    @Query("DELETE FROM recipes")
    suspend fun clearRecipes()

    // ingredientes con qty/unit via JOIN
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

    @Query("SELECT * FROM recipes ORDER BY title ASC")
    suspend fun getAllRecipeEntities(): List<RecipeEntity>

    @Query("SELECT * FROM recipe_ingredients ORDER BY recipeId ASC, position ASC")
    suspend fun getAllCrossRefs(): List<RecipeIngredientsCrossRef>

    @androidx.room.Transaction
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