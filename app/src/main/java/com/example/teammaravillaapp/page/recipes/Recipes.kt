package com.example.teammaravillaapp.page.recipes

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.teammaravillaapp.R
import com.example.teammaravillaapp.component.BackButton
import com.example.teammaravillaapp.component.FilterRecipes
import com.example.teammaravillaapp.component.GeneralBackground
import com.example.teammaravillaapp.component.RecipeCard
import com.example.teammaravillaapp.component.Title

@Composable
fun Recipes(
    onBack: () -> Unit = {},
    onRecipeClick: (Int) -> Unit = {},
    vm: RecipesViewModel = hiltViewModel()
) {
    val uiState by vm.uiState.collectAsStateWithLifecycle()

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
                            onClick = { vm.setShowMine(false) }
                        )
                        FilterRecipes(
                            text = stringResource(R.string.recipes_filter_mine),
                            selected = uiState.showMine,
                            onClick = { vm.setShowMine(true) }
                        )
                    }
                }

                Spacer(Modifier.height(12.dp))

                when {
                    uiState.isLoading -> {
                        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            Text(
                                text = stringResource(R.string.common_loading),
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    uiState.isEmpty -> {
                        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            Text(
                                text = if (uiState.showMine)
                                    stringResource(R.string.recipes_empty_mine)
                                else
                                    stringResource(R.string.recipes_empty_all),
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    else -> {
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
                                    onToggleFavorite = { vm.toggleFavorite(item.recipe.id) },
                                    onClick = { onRecipeClick(item.recipe.id) }
                                )
                            }
                        }
                    }
                }
            }

            //Box(Modifier.align(Alignment.BottomStart)) {
            //    BackButton(onClick = onBack)
            //}
        }
    }
}