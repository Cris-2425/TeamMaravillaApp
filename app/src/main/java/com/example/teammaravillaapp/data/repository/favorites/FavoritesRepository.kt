package com.example.teammaravillaapp.data.repository.favorites

import kotlinx.coroutines.flow.Flow

/**
 * Contrato del repositorio de favoritos.
 *
 * Gestiona el estado de recetas marcadas como favoritas,
 * exponiendo una API reactiva pensada para la UI.
 *
 * ### Estrategia de datos
 * - Room como source of truth local.
 * - Sincronización remota best-effort asociada al usuario autenticado.
 *
 * Diseñado para:
 * - Toggle inmediato en UI.
 * - Persistencia local sin dependencia de red.
 * - Sincronización automática al iniciar sesión.
 */
interface FavoritesRepository {

    /**
     * Flujo reactivo con los IDs de recetas favoritas.
     *
     * - Ideal para marcar estados en listas y detalles.
     * - Emite automáticamente ante cualquier cambio local.
     */
    val favoriteIds: Flow<Set<Int>>

    /**
     * Alterna el estado de favorito de una receta.
     *
     * - Actualiza el estado local inmediatamente.
     * - Intenta sincronizar con el backend si hay sesión activa.
     *
     * @param recipeId ID de la receta.
     */
    suspend fun toggle(recipeId: Int)
}