package com.example.teammaravillaapp.data.remote.datasource.favorites

/**
 * Fuente de datos remota para los favoritos de recetas de un usuario.
 *
 * Permite:
 *  - Recuperar la lista de recetas favoritas de un usuario.
 *  - Guardar la lista de recetas favoritas de un usuario.
 */
interface RemoteFavoritesDataSource {

    /**
     * Obtiene los IDs de las recetas favoritas de un usuario.
     *
     * @param userId ID del usuario.
     * @return Conjunto de IDs de recetas favoritas. Devuelve vacío si no hay favoritos o si ocurre un error.
     */
    suspend fun getFavorites(userId: String): Set<Int>

    /**
     * Guarda los IDs de las recetas favoritas de un usuario en el backend.
     *
     * La lista se ordena antes de ser enviada.
     *
     * @param userId ID del usuario.
     * @param ids Conjunto de IDs de recetas favoritas.
     */
    suspend fun saveFavorites(userId: String, ids: Set<Int>)
}