package com.example.teammaravillaapp.data.repository.recipes

import com.example.teammaravillaapp.model.IngredientLine
import com.example.teammaravillaapp.model.RecipeWithIngredients
import kotlinx.coroutines.flow.Flow

/**
 * Contrato del repositorio de recetas.
 *
 * Provee acceso reactivo y síncrono al conjunto de recetas y sus ingredientes,
 * abstractando la fuente de datos (Room, red, cache, seed inicial).
 *
 * ### Responsabilidades
 * - Exponer el listado de recetas como flujo reactivo.
 * - Permitir la observación de recetas individuales.
 * - Proveer acceso puntual (one-shot) cuando no se requiere reactividad.
 * - Gestionar la inicialización de datos base (seed).
 *
 * ### Principios aplicados
 * - **Reactive first**: la UI consume `Flow` para mantenerse sincronizada.
 * - **Single source of truth**: el repositorio define el estado vigente.
 * - **Desacoplamiento**: la UI no conoce el origen de los datos.
 */
interface RecipesRepository {

    /**
     * Flujo reactivo con la lista completa de recetas y sus ingredientes.
     *
     * - Emite automáticamente ante cualquier cambio en la fuente de datos.
     * - Ideal para pantallas de listado o dashboards.
     *
     * La implementación típica se basa en Room + Flow.
     */
    val recipes: Flow<List<RecipeWithIngredients>>

    /**
     * Observa una receta específica por su identificador.
     *
     * @param id ID de la receta.
     * @return un [Flow] que emite:
     * - la receta cuando existe,
     * - `null` si fue eliminada o aún no está disponible.
     *
     * Útil para pantallas de detalle.
     */
    fun observeRecipe(id: Int): Flow<RecipeWithIngredients?>

    /**
     * Inicializa la base de datos con datos por defecto si está vacía.
     *
     * ### Uso esperado
     * - Ejecutarse una sola vez (por ejemplo, al iniciar la app).
     * - No sobrescribe datos existentes.
     *
     * Ideal para seeds locales o datos demo.
     */
    suspend fun seedIfEmpty()

    /**
     * Obtiene una receta específica de forma no reactiva.
     *
     * @param id ID de la receta.
     * @return la receta si existe, o `null` en caso contrario.
     *
     * Útil para:
     * - validaciones puntuales
     * - lógica de negocio que no requiere observar cambios.
     */
    suspend fun getRecipe(id: Int): RecipeWithIngredients?

    /**
     * Observa los ingredientes de una receta con cantidad y unidad.
     *
     * A diferencia de [observeRecipe], este método expone únicamente
     * la información necesaria para vistas centradas en ingredientes
     * (por ejemplo, pasos de cocina o lista de compras).
     *
     * @param recipeId ID de la receta.
     * @return flujo reactivo de líneas de ingredientes.
     */
    fun observeIngredientLines(recipeId: Int): Flow<List<IngredientLine>>
}