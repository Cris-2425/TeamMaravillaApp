package com.example.teammaravillaapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.teammaravillaapp.data.FakeUserLists
import com.example.teammaravillaapp.model.RecipeData
import com.example.teammaravillaapp.page.CategoryFilter
import com.example.teammaravillaapp.page.CreateListt
import com.example.teammaravillaapp.page.Home
import com.example.teammaravillaapp.page.ListDetail
import com.example.teammaravillaapp.page.ListDetailContent
import com.example.teammaravillaapp.page.ListViewTypes
import com.example.teammaravillaapp.page.Login
import com.example.teammaravillaapp.page.Profile
import com.example.teammaravillaapp.page.Recipes
import com.example.teammaravillaapp.page.RecipesDetail
import com.example.teammaravillaapp.ui.theme.TeamMaravillaAppTheme

/**
 * # MainActivity
 *
 * Punto de entrada principal de la aplicación **Team Maravilla**.
 *
 * Carga el tema global de la app y renderiza las pantallas principales dentro del bloque `setContent`.
 * Actualmente no usa navegación, por lo que **todas las pantallas se muestran simultáneamente en stack**.
 *
 * En futuras versiones, aquí se integrará el `NavHost` o el gestor de navegación para
 * mostrar solo una pantalla a la vez.
 *
 * ---
 * ## Estructura actual:
 * - `CategoryFilter()` → Filtro de categorías de productos.
 * - `CreateListt()` → Creación de nuevas listas.
 * - `Home()` → Pantalla principal (inicio).
 * - `ListDetail()` y `ListDetailContent()` → Detalle visual de una lista.
 * - `ListViewTypes()` → Selector de estilo de listas.
 * - `Login()` → Formulario de acceso/registro.
 * - `Profile()` → Vista de perfil del usuario.
 * - `Recipes()` → Catálogo de recetas.
 * - `RecipesDetail()` → Detalle de una receta específica.
 *
 * ---
 * **Nota:**
 * Se invocan directamente todos los composables principales solo para propósitos de **visualización previa y testeo rápido**.
 * En un proyecto con navegación real, solo se renderizaría una pantalla por vez.
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TeamMaravillaAppTheme {
                /** Bloque de pruebas: se muestran todas las pantallas en orden */
                // CategoryFilter()
                // CreateListt()
                // Home()
                // ListDetail()
                val sample = FakeUserLists.sample()
                ListDetailContent(userList = sample)
                // ListViewTypes()
                // Login()
                // Profile()
                // Recipes()
                // RecipesDetail(RecipeData.recipes.first())
            }
        }
    }
}