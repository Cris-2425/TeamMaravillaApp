package com.example.teammaravillaapp.data

import android.util.Log
import com.example.teammaravillaapp.model.Recipe
import com.example.teammaravillaapp.util.TAG_GLOBAL

/**
 * Repositorio en memoria de recetas del usuario (favoritas / “Mis Recetas”).
 */
object FakeUserRecipes {

    private val userRecipes = mutableListOf<Recipe>()

    /** Devuelve una copia inmutable de las recetas del usuario. */
    fun all(): List<Recipe> = userRecipes.toList()

    /** ¿La receta ya está guardada como del usuario? */
    fun contains(recipe: Recipe): Boolean =
        userRecipes.any { it.title == recipe.title }

    /** Añade la receta si no existe. */
    fun add(recipe: Recipe) {
        if (contains(recipe)) return
        userRecipes.add(recipe)
        Log.e(TAG_GLOBAL, "FakeUserRecipes → Añadida '${recipe.title}'")
    }

    /** Elimina la receta si existe. */
    fun remove(recipe: Recipe) {
        val removed = userRecipes.removeAll { it.title == recipe.title }
        if (removed) {
            Log.e(TAG_GLOBAL, "FakeUserRecipes → Eliminada '${recipe.title}'")
        }
    }

    /** Limpia todas las recetas del usuario. */
    fun clear() {
        userRecipes.clear()
        Log.e(TAG_GLOBAL, "FakeUserRecipes → Limpiado")
    }
}
