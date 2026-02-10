package com.example.teammaravillaapp.page.recipesdetail

import com.example.teammaravillaapp.model.Product
import com.example.teammaravillaapp.model.Recipe

/**
 * Estado de UI para la pantalla de detalle de receta.
 *
 * Representa de forma inmutable todo lo que la UI necesita:
 * - indicadores de carga / receta no encontrada
 * - receta e ingredientes ya resueltos (listos para pintar)
 * - estado de favorito y posibles errores ya “humanizados” si procede
 *
 * Motivo:
 * - Evitar que la UI tenga que calcular derivadas complejas.
 * - Facilitar previews y tests (estado determinista).
 *
 * @property isLoading Indica si se está cargando información.
 * @property isNotFound Indica que no existe la receta solicitada (404 lógico).
 * @property error Mensaje de error (si lo usas) para casos no cubiertos por notFound.
 * Restricciones: puede ser null si no hay error.
 * @property recipe Receta principal a mostrar.
 * Restricciones: normalmente no nula cuando isNotFound=false y isLoading=false.
 * @property ingredients Ingredientes ya enriquecidos (si existe catalog/Room/API).
 * @property isFavorite True si la receta actual está marcada como favorita.
 *
 * Ejemplo de uso:
 * {@code
 * val st = RecipesDetailUiState(
 *   isLoading = false,
 *   recipe = recipe,
 *   ingredients = products,
 *   isFavorite = true
 * )
 * }
 */
data class RecipesDetailUiState(
    val isLoading: Boolean = true,
    val isNotFound: Boolean = false,
    val error: String? = null,
    val recipe: Recipe? = null,
    val ingredients: List<Product> = emptyList(),
    val isFavorite: Boolean = false
)