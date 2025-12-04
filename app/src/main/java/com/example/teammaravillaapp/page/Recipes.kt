package com.example.teammaravillaapp.page

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.teammaravillaapp.R
import com.example.teammaravillaapp.component.BackButton
import com.example.teammaravillaapp.component.FilterRecipes
import com.example.teammaravillaapp.component.GeneralBackground
import com.example.teammaravillaapp.component.RecipeCard
import com.example.teammaravillaapp.component.Title
import com.example.teammaravillaapp.data.FakeUserRecipes
import com.example.teammaravillaapp.model.Recipe
import com.example.teammaravillaapp.model.RecipeData
import com.example.teammaravillaapp.ui.theme.TeamMaravillaAppTheme

/**
 * # Pantalla: **Recetas**
 *
 * Lista de recetas con un filtro simple:
 * - **Todos**: muestra [recipes].
 * - **Mis Recetas**: muestra las guardadas en [FakeUserRecipes].
 *
 * @param onBack Acción al pulsar el botón de volver.
 * @param onRecipeClick Acción al pulsar una receta (navegación al detalle).
 * @param recipes Fuente por defecto (recetas base de la app).
 */
@Composable
fun Recipes(
    onBack: () -> Unit,
    onRecipeClick: (Int) -> Unit,
    recipes: List<Recipe> = RecipeData.recipes
) {
    var showMine by remember { mutableStateOf(false) }

    val allRecipes = recipes
    val myRecipes = FakeUserRecipes.all()
    val visible =
        if (showMine) myRecipes
        else allRecipes

    Box(Modifier.fillMaxSize()) {
        GeneralBackground()

        Column(
            Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
        ) {
            Spacer(Modifier.height(20.dp))

            Title(
                texto = if (showMine)
                    stringResource(R.string.recipes_title_mine)
                else
                    stringResource(R.string.recipes_title_all)
            )

            Spacer(Modifier.height(16.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    Modifier.clickable { showMine = false }
                ) {
                    FilterRecipes(
                        text = stringResource(R.string.recipes_filter_all),
                        selected = !showMine
                    )
                }
                Box(
                    Modifier.clickable { showMine = true }
                ) {
                    FilterRecipes(
                        text = stringResource(R.string.recipes_filter_mine),
                        selected = showMine
                    )
                }
            }

            Spacer(Modifier.height(16.dp))

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(18.dp),
                contentPadding = PaddingValues(bottom = 120.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(visible) { rec ->
                    // Al pulsar la tarjeta, navegamos al detalle de esa receta
                    Box(
                        modifier = Modifier.clickable {
                            onRecipeClick(rec.id)
                        }
                    ) {
                        RecipeCard(recipe = rec)
                    }
                }
            }
        }

        Box(Modifier.align(Alignment.BottomStart)) {
            BackButton(onClick = onBack)
        }
    }
}

@Preview
@Composable
fun PreviewRecipes() {
    TeamMaravillaAppTheme {
        Recipes(
            onBack = {},
            onRecipeClick = {}
        )
    }
}