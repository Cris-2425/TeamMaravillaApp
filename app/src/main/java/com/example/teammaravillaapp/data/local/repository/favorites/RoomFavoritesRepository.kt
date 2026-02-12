package com.example.teammaravillaapp.data.local.repository.favorites

import com.example.teammaravillaapp.data.local.dao.FavoritesDao
import com.example.teammaravillaapp.data.local.entity.FavoriteRecipeEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Implementación de [FavoritesRepository] respaldada por Room.
 *
 * Expone el estado de favoritos como un `Flow<Set<Int>>` para consumo reactivo en UI/estado,
 * manteniendo el dominio desacoplado del detalle de persistencia (DAO/entidades Room).
 *
 * ### Decisiones de diseño
 * - Se transforma `List<Int>` → `Set<Int>` para que el consumidor trabaje con semántica de conjunto
 *   (pertenencia O(1) y sin duplicados).
 * - `replaceAll` ordena los IDs antes de persistir para:
 *   - Reducir diffs no deterministas en pruebas/sincronización.
 *   - Mantener inserciones reproducibles (aunque no afecte al resultado lógico).
 *
 * ## Concurrencia
 * - Las operaciones `suspend` delegan en Room (thread-safe).
 * - El `Flow` re-emite automáticamente ante cambios en tabla.
 *
 * @property dao DAO de favoritos, inyectado por Hilt.
 *
 * @see FavoritesDao
 * @see FavoriteRecipeEntity
 */
@Singleton
class RoomFavoritesRepository @Inject constructor(
    private val dao: FavoritesDao
) {

    /**
     * Observa el conjunto de IDs favoritos en tiempo real.
     *
     * @return `Flow` que emite un `Set` de IDs favoritos.
     */
    fun observeIds(): Flow<Set<Int>> =
        dao.observeIds().map { it.toSet() }

    /**
     * Comprueba si una receta está marcada como favorita.
     *
     * @param recipeId ID de la receta.
     * @return `true` si el ID existe en favoritos.
     */
    suspend fun isFavorite(recipeId: Int): Boolean =
        dao.isFavorite(recipeId)

    /**
     * Marca una receta como favorita.
     *
     * Operación idempotente: si ya existe, el DAO ignora el conflicto.
     *
     * @param recipeId ID de la receta.
     */
    suspend fun add(recipeId: Int) {
        dao.add(FavoriteRecipeEntity(recipeId))
    }

    /**
     * Desmarca una receta como favorita.
     *
     * @param recipeId ID de la receta.
     */
    suspend fun remove(recipeId: Int) {
        dao.remove(recipeId)
    }

    /**
     * Reemplaza completamente el conjunto de favoritos persistidos.
     *
     * @param ids Conjunto final de IDs favoritos.
     */
    suspend fun replaceAll(ids: Set<Int>) {
        dao.replaceAll(ids.sorted().map { FavoriteRecipeEntity(it) })
    }

    /**
     * Obtiene todos los IDs favoritos en una lectura única (*snapshot*).
     *
     * @return Conjunto de IDs favoritos.
     */
    suspend fun getAllIdsOnce(): Set<Int> =
        dao.getAllIdsOnce().toSet()

}