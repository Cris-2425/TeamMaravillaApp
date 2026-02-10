package com.example.teammaravillaapp.data.local.repository.favorites

import com.example.teammaravillaapp.data.local.dao.FavoritesDao
import com.example.teammaravillaapp.data.local.entity.FavoriteRecipeEntity
import com.example.teammaravillaapp.data.repository.favorites.FavoritesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Repositorio que gestiona los favoritos de recetas utilizando Room.
 *
 * Implementa [FavoritesRepository] para mantener la capa de dominio desacoplada de la implementación de Room.
 * Proporciona un flujo reactivo de IDs favoritos y permite alternar el estado de favorito.
 *
 * @property dao Instancia de [FavoritesDao] inyectada por Hilt.
 */
@Singleton
class RoomFavoritesRepository @Inject constructor(
    private val dao: FavoritesDao
) : FavoritesRepository {

    /**
     * Flujo reactivo de IDs de recetas favoritas.
     *
     * Cada vez que se agregue o elimine un favorito, este flujo emitirá un nuevo [Set] actualizado.
     */
    override val favoriteIds: Flow<Set<Int>> =
        dao.observeIds()
            .map { it.toSet() }

    /**
     * Alterna el estado de favorito de una receta.
     *
     * Si la receta ya es favorita, se elimina; si no lo es, se agrega.
     *
     * @param recipeId ID de la receta.
     */
    override suspend fun toggle(recipeId: Int) {
        val isFav = dao.isFavorite(recipeId)
        if (isFav) dao.remove(recipeId)
        else dao.add(FavoriteRecipeEntity(recipeId))
    }
}