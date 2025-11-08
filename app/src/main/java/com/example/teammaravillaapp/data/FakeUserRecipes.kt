package com.example.teammaravillaapp.data

import android.util.Log
import com.example.teammaravillaapp.model.Recipe
import com.example.teammaravillaapp.util.TAG_GLOBAL

/**
 * Repositorio **en memoria** para “Mis Recetas” (favoritas del usuario).
 *
 * Clave natural: el **título** de la receta. No se duplican títulos.
 * Ideal para la pestaña “Mis Recetas” y filtros en la pantalla Recipes.
 */
object FakeUserRecipes {

    /** Almacén de recetas del usuario. */
    private val userRecipes = mutableListOf<Recipe>()

    /**
     * Devuelve una copia inmutable del listado.
     */
    fun all(): List<Recipe> = userRecipes.toList()

    /**
     * ¿La receta ya está guardada?
     *
     * @return `true` si ya existe una receta con el mismo título.
     */
    fun contains(recipe: Recipe): Boolean =
        userRecipes.any { it.title == recipe.title }

    /**
     * Añade la receta si **no** existe (según título).
     */
    fun add(recipe: Recipe) {
        if (contains(recipe)) return
        userRecipes.add(recipe)
        Log.e(TAG_GLOBAL, "FakeUserRecipes → Añadida '${recipe.title}'")
    }

    /**
     * Elimina la receta si existe (por título).
     */
    fun remove(recipe: Recipe) {
        val removed = userRecipes.removeAll { it.title == recipe.title }
        if (removed) {
            Log.e(TAG_GLOBAL, "FakeUserRecipes → Eliminada '${recipe.title}'")
        }
    }

    /**
     * Limpia todas las recetas guardadas del usuario.
     */
    fun clear() {
        userRecipes.clear()
        Log.e(TAG_GLOBAL, "FakeUserRecipes → Limpiado")
    }
}