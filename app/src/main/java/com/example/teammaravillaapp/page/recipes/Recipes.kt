package com.example.teammaravillaapp.page.recipes

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.teammaravillaapp.R
import com.example.teammaravillaapp.component.BackButton
import com.example.teammaravillaapp.component.FilterRecipes
import com.example.teammaravillaapp.component.GeneralBackground
import com.example.teammaravillaapp.component.RecipeCard
import com.example.teammaravillaapp.component.Title
import com.example.teammaravillaapp.ui.theme.TeamMaravillaAppTheme

@Composable
fun Recipes(
    onBack: () -> Unit = {},
    onRecipeClick: (Int) -> Unit = {},
    recipesViewModel: RecipesViewModel = hiltViewModel()
) {
    val uiState by recipesViewModel.uiState.collectAsState()

    Box(Modifier.fillMaxSize()) {
        GeneralBackground(overlayAlpha = 0.20f) {

            Column(
                Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp)
            ) {
                Spacer(Modifier.height(18.dp))

                Title(
                    texto = if (uiState.showMine)
                        stringResource(R.string.recipes_title_mine)
                    else
                        stringResource(R.string.recipes_title_all)
                )

                Spacer(Modifier.height(12.dp))

                Surface(
                    shape = MaterialTheme.shapes.large,
                    tonalElevation = 1.dp,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(10.dp),
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        FilterRecipes(
                            text = stringResource(R.string.recipes_filter_all),
                            selected = !uiState.showMine,
                            onClick = { recipesViewModel.showAll() }
                        )
                        FilterRecipes(
                            text = stringResource(R.string.recipes_filter_mine),
                            selected = uiState.showMine,
                            onClick = { recipesViewModel.showMine() }
                        )
                    }
                }

                Spacer(Modifier.height(12.dp))

                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(14.dp),
                    contentPadding = PaddingValues(bottom = 120.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(uiState.visibleRecipes) { item ->
                        RecipeCard(
                            recipe = item.recipe,
                            ingredients = item.ingredients,
                            isFavorite = item.recipe.id in uiState.favoriteIds,
                            onToggleFavorite = { recipesViewModel.toggleFavorite(item.recipe.id) },
                            onClick = { onRecipeClick(item.recipe.id) }
                        )
                    }
                }
            }

            Box(Modifier.align(Alignment.BottomStart)) {
                BackButton(onClick = onBack)
            }
        }
    }
}

@Preview
@Composable
fun PreviewRecipes() {
    TeamMaravillaAppTheme { Recipes() }
}