package com.example.teammaravillaapp.page.recipesdetail.usecase

import com.example.teammaravillaapp.data.repository.favorites.FavoritesRepository
import javax.inject.Inject

/**
 * Caso de uso para alternar el estado de “favorito” de una receta.
 *
 * Centraliza la intención de negocio de marcar/desmarcar una receta como favorita, evitando que la capa
 * de presentación (ViewModel/UI) tenga que conocer detalles del repositorio.
 *
 * Motivo:
 * - Mantener la capa de dominio clara y reutilizable.
 * - Facilitar testeo unitario (mock del repositorio).
 *
 * @param repo Repositorio que gestiona la persistencia del conjunto de favoritos.
 * Restricciones:
 * - No debe ser nulo.
 *
 * @see FavoritesRepository
 *
 * Ejemplo de uso:
 * {@code
 * viewModelScope.launch {
 *   toggleFavoriteUseCase.run(recipeId)
 * }
 * }
 */
class ToggleFavoriteUseCase @Inject constructor(
    private val repo: FavoritesRepository
) {

    /**
     * Alterna el estado favorito para una receta concreta.
     *
     * Si la receta ya está marcada como favorita, se elimina del conjunto; si no lo está, se añade.
     *
     * @param id Identificador de la receta.
     * Restricciones:
     * - Debe ser un id válido (> 0). Si se pasa un valor inválido, el comportamiento depende del repositorio.
     *
     * @throws IllegalArgumentException Posible validación de negocio si el repositorio rechaza ids inválidos.
     * @throws Exception Excepción técnica (IO/DataStore/Room) si la operación no puede persistirse.
     *
     * @see FavoritesRepository.toggle
     *
     * Ejemplo de uso:
     * {@code
     * toggleFavoriteUseCase.run(12)
     * }
     */
    suspend fun run(id: Int) = repo.toggle(id)
}