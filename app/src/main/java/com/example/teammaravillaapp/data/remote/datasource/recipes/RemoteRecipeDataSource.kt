package com.example.teammaravillaapp.data.remote.datasource.recipes

import com.example.teammaravillaapp.data.remote.dto.RecipeDto

/**
 * Fuente de datos remota para recetas.
 *
 * Define los contratos para obtener y sobrescribir recetas desde/hacia el backend.
 * La implementación concreta puede usar Retrofit, OkHttp u otra librería HTTP.
 */
interface RemoteRecipesDataSource {

    /**
     * Obtiene todas las recetas disponibles en el backend.
     *
     * @return Lista de [RecipeDto] con todos los datos de recetas.
     */
    suspend fun fetchAll(): List<RecipeDto>

    /**
     * Sobrescribe todas las recetas en el backend con la lista proporcionada.
     *
     * Esta operación reemplaza completamente el estado remoto.
     *
     * @param recipes Lista de [RecipeDto] a enviar al backend.
     */
    suspend fun overwriteAll(recipes: List<RecipeDto>)
}