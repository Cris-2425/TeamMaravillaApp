package com.example.teammaravillaapp.page.recipes

import com.example.teammaravillaapp.model.RecipeWithIngredients

/**
 * Estado de UI de la pantalla de Recetas.
 *
 * Representa la “foto” completa que necesita la UI para pintar:
 * - si está cargando
 * - si hay error
 * - qué filtro está activo
 * - qué recetas se muestran y cuáles son favoritas
 *
 * Motivo:
 * - Centraliza decisiones para que la UI sea declarativa y simple.
 *
 * @property isLoading Indica carga inicial o refresco. Restricciones: true implica que la UI debe priorizar loading.
 * @property error Error técnico de carga (network, parse, etc.). Restricciones: null si no hay error.
 * @property showMine Si es true, se visualizan solo las recetas “marcadas como favoritas”.
 * @property favoriteIds Conjunto de ids de recetas favoritas. Restricciones: ids > 0 recomendado.
 * @property visibleRecipes Lista final ya filtrada que la UI debe renderizar.
 *
 * @see RecipesViewModel Orquestación del estado.
 */
data class RecipesUiState(
    val isLoading: Boolean = true,
    val error: Throwable? = null,
    val showMine: Boolean = false,
    val favoriteIds: Set<Int> = emptySet(),
    val visibleRecipes: List<RecipeWithIngredients> = emptyList()
) {
    /**
     * Indica si la pantalla está vacía (no hay recetas para mostrar) en un estado “estable”.
     *
     * @return true si:
     * - no está cargando
     * - no hay error
     * - y la lista visible está vacía
     */
    val isEmpty: Boolean get() = !isLoading && error == null && visibleRecipes.isEmpty()
}