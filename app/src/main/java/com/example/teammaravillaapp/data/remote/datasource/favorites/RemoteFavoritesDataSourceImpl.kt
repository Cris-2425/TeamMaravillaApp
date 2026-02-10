package com.example.teammaravillaapp.data.remote.datasource.favorites

import com.example.teammaravillaapp.data.remote.api.JsonStorageApi
import com.example.teammaravillaapp.data.remote.dto.FavoritesDto
import com.google.gson.Gson
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Implementación de [RemoteFavoritesDataSource] usando un almacenamiento JSON genérico ([JsonStorageApi]).
 *
 * Maneja la persistencia de favoritos de manera simple:
 * - Recupera un archivo JSON por usuario.
 * - Guarda un archivo JSON por usuario.
 *
 * Si ocurre cualquier error (ej. 404) al leer los favoritos, se devuelve un conjunto vacío.
 */
@Singleton
class RemoteFavoritesDataSourceImpl @Inject constructor(
    private val api: JsonStorageApi
) : RemoteFavoritesDataSource {

    private val gson = Gson()
    private val folder = "favorites"

    /**
     * Obtiene los IDs de recetas favoritas de un usuario.
     *
     * Devuelve un conjunto vacío si no existe archivo o hay error.
     */
    override suspend fun getFavorites(userId: String): Set<Int> =
        runCatching {
            val json = api.getFile(folder, userId)
            gson.fromJson(json, FavoritesDto::class.java).recipeIds.toSet()
        }.getOrElse { emptySet() }

    /**
     * Guarda los IDs de recetas favoritas de un usuario.
     *
     * La lista se convierte a JSON y se ordena antes de guardar.
     */
    override suspend fun saveFavorites(userId: String, ids: Set<Int>) {
        api.saveFile(folder, userId, FavoritesDto(ids.toList().sorted()))
    }
}