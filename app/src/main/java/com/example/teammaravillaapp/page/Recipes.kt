package com.example.teammaravillaapp.page

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.teammaravillaapp.component.BackButton
import com.example.teammaravillaapp.component.FilterRecipes
import com.example.teammaravillaapp.component.GeneralBackground
import com.example.teammaravillaapp.component.RecipeCard
import com.example.teammaravillaapp.component.Title
import com.example.teammaravillaapp.data.FakeUserRecipes
import com.example.teammaravillaapp.model.Recipe
import com.example.teammaravillaapp.model.RecipeData
import com.example.teammaravillaapp.ui.theme.TeamMaravillaAppTheme

@Composable
fun Recipes(
    recipes: List<Recipe> = RecipeData.recipes
) {
    var showMine by remember { mutableStateOf(false) }

    // Fuente de datos visible según filtro
    val allRecipes = recipes
    val myRecipes = FakeUserRecipes.all()
    val visible = if (showMine) myRecipes else allRecipes

    Box(Modifier.fillMaxSize()) {

        GeneralBackground()

        androidx.compose.foundation.layout.Column(
            Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
        ) {
            Spacer(Modifier.height(20.dp))

            Title(if (showMine) "Mis Recetas" else "Recetas")

            Spacer(Modifier.height(16.dp))

            // No modifico tu FilterRecipes: lo envuelvo en un clickable
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(Modifier.clickable { showMine = false }) {
                    FilterRecipes(text = "Todos", selected = !showMine)
                }
                Box(Modifier.clickable { showMine = true }) {
                    FilterRecipes(text = "Mis Recetas", selected = showMine)
                }
            }

            Spacer(Modifier.height(16.dp))

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(18.dp),
                contentPadding = PaddingValues(bottom = 120.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(visible) { rec ->
                    RecipeCard(recipe = rec)
                }
            }
        }

        Box(Modifier.align(Alignment.BottomStart)) {
            BackButton()
        }
    }
}

@Preview
@Composable
fun PreviewRecipes() {
    TeamMaravillaAppTheme {
        Recipes()
    }
}
