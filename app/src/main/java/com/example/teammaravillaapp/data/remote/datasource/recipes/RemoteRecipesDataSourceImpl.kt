package com.example.teammaravillaapp.data.remote.datasource.recipes

import com.example.teammaravillaapp.data.remote.api.RecipesApi
import com.example.teammaravillaapp.data.remote.dto.RecipeDto
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Implementación de [RemoteRecipesDataSource] usando Retrofit ([RecipesApi]).
 *
 * Se encarga de:
 * - Obtener todas las recetas del backend.
 * - Sobrescribir completamente las recetas en el backend.
 *
 * Usa un [Mutex] para serializar las operaciones de escritura y evitar condiciones de carrera.
 *
 * Anotado con [Singleton] para que Hilt provea una sola instancia.
 *
 * @property api Interfaz de Retrofit para llamadas de recetas.
 */
@Singleton
class RemoteRecipesDataSourceImpl @Inject constructor(
    private val api: RecipesApi
) : RemoteRecipesDataSource {

    private val writeMutex = Mutex()

    override suspend fun fetchAll(): List<RecipeDto> =
        api.getAll()

    override suspend fun overwriteAll(recipes: List<RecipeDto>) {
        writeMutex.withLock {
            api.saveAll(recipes)
        }
    }
}