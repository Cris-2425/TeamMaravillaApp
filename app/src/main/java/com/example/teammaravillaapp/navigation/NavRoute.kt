package com.example.teammaravillaapp.navigation

/**
 * Definición centralizada de rutas de navegación de la app.
 *
 * Objetivo:
 * - Evitar strings sueltos (“magic strings”) por el proyecto.
 * - Centralizar argumentos y helpers `createRoute(...)`.
 *
 * Cada destino define:
 * - [route]: patrón de ruta consumido por Navigation Compose.
 * - Constantes `ARG_*` para argumentos.
 * - Helpers `createRoute(...)` para construir rutas seguras.
 *
 * Recomendación:
 * - Si un argumento puede contener caracteres especiales (por ejemplo IDs con `/`, espacios, etc.),
 *   usa `Uri.encode(value)` al construir la ruta.
 */
sealed class NavRoute(val route: String) {

    /** Pantalla principal (Home). */
    data object Home : NavRoute("home")

    /** Pantalla de creación de lista. */
    data object CreateList : NavRoute("create_list")

    /**
     * Detalle de lista.
     *
     * @property route Patrón con argumento `{listId}`.
     * @see createRoute Para crear la ruta final.
     */
    data object ListDetail : NavRoute("list_detail/{listId}") {
        /** Nombre del argumento en el grafo: `listId`. */
        const val ARG_LIST_ID = "listId"

        /**
         * Construye la ruta final hacia detalle de lista.
         *
         * @param listId Id de lista a mostrar.
         * Restricciones: no blank.
         * @return Ruta final, por ejemplo `list_detail/abc123`.
         */
        fun createRoute(listId: String): String = "list_detail/$listId"
    }

    /** Listado de recetas. */
    data object Recipes : NavRoute("recipes")

    /**
     * Detalle de receta.
     */
    data object RecipesDetail : NavRoute("recipes_detail/{recipeId}") {
        const val ARG_RECIPE_ID = "recipeId"

        /**
         * @param recipeId Id numérico de la receta.
         * @return Ruta final `recipes_detail/10`.
         */
        fun createRoute(recipeId: Int): String = "recipes_detail/$recipeId"
    }

    /**
     * Pantalla para seleccionar una lista a la que añadir ingredientes de una receta.
     */
    data object SelectList : NavRoute("select_list/{recipeId}") {
        const val ARG_RECIPE_ID = "recipeId"
        fun createRoute(recipeId: Int): String = "select_list/$recipeId"
    }

    /** Perfil de usuario. */
    data object Profile : NavRoute("profile")

    /** Login. */
    data object Login : NavRoute("login")

    /** Ajuste del tipo de vista de listas. */
    data object ListViewTypes : NavRoute("list_view_types")

    /** Filtro por categoría. */
    data object CategoryFilter : NavRoute("category_filter")

    /**
     * Cámara (con argumento opcional `listId` como query param).
     *
     * Nota: si `listId` puede contener caracteres especiales, encódalo con `Uri.encode`.
     */
    data object Camera : NavRoute("camera?listId={listId}") {
        const val ARG_LIST_ID = "listId"

        /**
         * @param listId Id opcional de lista a la que asociar el ticket.
         * @return `camera` si no hay id, o `camera?listId=...` si existe.
         */
        fun createRoute(listId: String? = null): String =
            if (listId.isNullOrBlank()) "camera" else "camera?listId=$listId"
    }

    /** Splash inicial. */
    data object Splash : NavRoute("splash")

    /** Ajustes. */
    data object Settings : NavRoute("settings")

    /** Estadísticas. */
    data object Stats : NavRoute("stats")

    /** Ayuda. */
    data object Help : NavRoute("help")

    /** Historial. */
    data object History : NavRoute("history")

    /** Registro. */
    data object Register : NavRoute("register")
}