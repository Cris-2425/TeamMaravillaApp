package com.example.teammaravillaapp.data.remote.dto
import com.example.teammaravillaapp.data.repository.favorites.FavoritesRepository
/**
 * DTO que representa la lista de recetas favoritas de un usuario desde la API.
 *
 * Se utiliza para:
 * - Obtener o persistir la lista de IDs de recetas favoritas del usuario.
 * - Mapear a la capa de dominio ([Set<Int>] en [FavoritesRepository]).
 *
 * @property recipeIds Lista de identificadores de recetas favoritas. Por defecto vacía.
 */
data class FavoritesDto(
    val recipeIds: List<Int> = emptyList()
)