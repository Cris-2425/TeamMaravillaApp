package com.example.teammaravillaapp.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.teammaravillaapp.data.local.entity.FavoriteRecipeEntity
import kotlinx.coroutines.flow.Flow

/**
 * DAO para la gestión de recetas favoritas del usuario.
 *
 * Proporciona operaciones CRUD sobre la tabla `favorite_recipes`
 * y permite observar cambios en tiempo real mediante Flows.
 */
@Dao
interface FavoritesDao {

    /**
     * Observa los IDs de las recetas favoritas en tiempo real.
     * Se emite un nuevo valor cada vez que cambia la tabla.
     */
    @Query("SELECT recipeId FROM favorite_recipes")
    fun observeIds(): Flow<List<Int>>

    /**
     * Inserta una receta favorita.
     * Si ya existe, se ignora el conflicto.
     */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun add(entity: FavoriteRecipeEntity)

    /**
     * Inserta múltiples recetas favoritas.
     * Conflictos se ignoran.
     */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addAll(items: List<FavoriteRecipeEntity>)

    /** Elimina una receta favorita por su ID. */
    @Query("DELETE FROM favorite_recipes WHERE recipeId = :recipeId")
    suspend fun remove(recipeId: Int)

    /** Verifica si un ID de receta está marcado como favorito. */
    @Query("SELECT EXISTS(SELECT 1 FROM favorite_recipes WHERE recipeId = :recipeId)")
    suspend fun isFavorite(recipeId: Int): Boolean

    /** Borra todas las recetas favoritas. */
    @Query("DELETE FROM favorite_recipes")
    suspend fun clearAll()

    /** Obtiene todos los IDs de recetas favoritas una única vez (sin Flow). */
    @Query("SELECT recipeId FROM favorite_recipes")
    suspend fun getAllIdsOnce(): List<Int>
}