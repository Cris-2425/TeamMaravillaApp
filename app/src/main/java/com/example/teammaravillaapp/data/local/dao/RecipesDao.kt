package com.example.teammaravillaapp.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.example.teammaravillaapp.data.local.entity.RecipeEntity
import com.example.teammaravillaapp.data.local.entity.RecipeIngredientCrossRef
import com.example.teammaravillaapp.data.local.entity.RecipeWithProductsRoom
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

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertCrossRefs(items: List<RecipeIngredientCrossRef>)

    @Query("DELETE FROM recipe_ingredients")
    suspend fun clearCrossRefs()

    @Query("DELETE FROM recipes")
    suspend fun clearRecipes()
}