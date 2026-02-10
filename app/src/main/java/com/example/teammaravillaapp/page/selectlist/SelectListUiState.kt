package com.example.teammaravillaapp.page.selectlist

import com.example.teammaravillaapp.model.Recipe
import com.example.teammaravillaapp.model.UserList

/**
 * Estado de UI para la pantalla de selección de lista (añadir ingredientes).
 *
 * Contiene toda la información necesaria para renderizar la pantalla sin lógica adicional:
 * - estados de carga y no encontrado (receta)
 * - receta objetivo (si existe)
 * - listas disponibles para seleccionar
 * - flag de guardado para deshabilitar acciones repetidas
 *
 * Motivo:
 * - Mantener la UI declarativa y fácilmente “previewable”.
 * - Evitar que la UI acceda a repositorios o combine flujos.
 *
 * @property isLoading Indica si se está cargando información inicial.
 * @property isRecipeNotFound True si la receta solicitada no existe o el id es inválido.
 * @property recipe Receta objetivo de la operación.
 * Restricciones:
 * - Normalmente no nula cuando isRecipeNotFound=false e isLoading=false.
 * @property lists Listas disponibles para elegir.
 * @property isSaving True cuando se está persistiendo la operación (añadir ingredientes).
 *
 * Ejemplo de uso:
 * {@code
 * val st = SelectListUiState(
 *   isLoading = false,
 *   recipe = recipe,
 *   lists = lists,
 *   isSaving = false
 * )
 * }
 */
data class SelectListUiState(
    val isLoading: Boolean = true,
    val isRecipeNotFound: Boolean = false,
    val recipe: Recipe? = null,
    val lists: List<UserList> = emptyList(),
    val isSaving: Boolean = false
)