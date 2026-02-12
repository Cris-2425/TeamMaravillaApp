package com.example.teammaravillaapp.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.example.teammaravillaapp.data.local.entity.FavoriteRecipeEntity
import kotlinx.coroutines.flow.Flow

/**
 * Acceso a datos de **recetas favoritas** del usuario.
 *
 * Este DAO modela una tabla de “marcados” (`favorite_recipes`) cuyo estado debe ser:
 * - **Observable** (UI reactiva mediante `Flow`)
 * - **Idempotente** ante inserciones repetidas (se ignoran duplicados)
 *
 * ## Concurrencia
 * - Los métodos `suspend` se ejecutan en el *dispatcher* de Room (no requieren `Dispatchers.IO`).
 * - Los `Flow` emitidos por Room son **cold** y re-emiten automáticamente cuando cambia la tabla.
 *
 * @see FavoriteRecipeEntity
 */
@Dao
interface FavoritesDao {

    /**
     * Observa el conjunto de IDs marcados como favorito.
     *
     * Diseñado para alimentar UI/estado en tiempo real: cada inserción/borrado en `favorite_recipes`
     * provoca una nueva emisión.
     *
     * ## Concurrencia
     * Room gestiona la invalidación y la recolección del `Flow` de forma segura para concurrencia.
     *
     * @return `Flow` con la lista de `recipeId` favoritos (puede estar vacía).
     */
    @Query("SELECT recipeId FROM favorite_recipes")
    fun observeIds(): Flow<List<Int>>

    /**
     * Marca una receta como favorita.
     *
     * Se usa **IGNORE** para que la operación sea idempotente: si el `recipeId` ya existe,
     * no se lanza error y no se modifica el estado.
     *
     * @param entity Entidad que representa el vínculo “receta favorita”.
     */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun add(entity: FavoriteRecipeEntity)

    /**
     * Marca múltiples recetas como favoritas.
     *
     * Mantiene la misma semántica idempotente que [add]: los conflictos se **ignoran** para evitar
     * fallos por duplicados cuando se sincroniza desde varias fuentes (ej. remoto + cache).
     *
     * @param items Colección de entidades favoritas a insertar.
     */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addAll(items: List<FavoriteRecipeEntity>)

    /**
     * Desmarca una receta como favorita.
     *
     * @param recipeId ID de la receta a eliminar de favoritos.
     */
    @Query("DELETE FROM favorite_recipes WHERE recipeId = :recipeId")
    suspend fun remove(recipeId: Int)

    /**
     * Comprueba si una receta está marcada como favorita.
     *
     * @param recipeId ID de la receta.
     * @return `true` si existe un registro en `favorite_recipes` para ese ID.
     */
    @Query("SELECT EXISTS(SELECT 1 FROM favorite_recipes WHERE recipeId = :recipeId)")
    suspend fun isFavorite(recipeId: Int): Boolean

    /**
     * Elimina todos los favoritos.
     *
     * Útil para *logout*, cambio de perfil o reseteos de caché local.
     */
    @Query("DELETE FROM favorite_recipes")
    suspend fun clearAll()

    /**
     * Recupera todos los IDs favoritos en una única lectura (*snapshot*).
     *
     * Preferible a [observeIds] cuando no se necesita reactividad (ej. sincronización puntual).
     *
     * @return Lista de `recipeId` favoritos (puede estar vacía).
     */
    @Query("SELECT recipeId FROM favorite_recipes")
    suspend fun getAllIdsOnce(): List<Int>

    /**
     * Reemplaza por completo el conjunto de favoritos de forma atómica.
     *
     * Se implementa como:
     * 1) `DELETE` total
     * 2) inserción condicional si hay elementos
     *
     * Esto evita estados intermedios observables (parciales) y simplifica la sincronización
     * “fuente de verdad” (por ejemplo, tras descargar favoritos desde remoto).
     *
     * ## Concurrencia
     * Marcado con `@Transaction` para garantizar atomicidad: o se aplica todo el reemplazo o nada.
     *
     * @param items Nuevo conjunto de favoritos a persistir.
     */
    @Transaction
    suspend fun replaceAll(items: List<FavoriteRecipeEntity>) {
        clearAll()
        if (items.isNotEmpty()) addAll(items)
    }
}