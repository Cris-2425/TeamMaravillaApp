package com.example.teammaravillaapp.page.recipes

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.*
import androidx.lifecycle.viewmodel.compose.viewModel
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
import com.example.teammaravillaapp.ui.theme.TeamMaravillaAppTheme

@Composable
fun Recipes(
    onBack: () -> Unit = {},
    onRecipeClick: (Int) -> Unit = {},
    recipesViewModel: RecipesViewModel = viewModel()
) {
    val uiState by recipesViewModel.uiState.collectAsState()

    Box(Modifier.fillMaxSize()) {
        GeneralBackground()

        Column(
            Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
        ) {
            Spacer(Modifier.height(20.dp))

            Title(
                texto = if (uiState.showMine)
                    stringResource(R.string.recipes_title_mine)
                else
                    stringResource(R.string.recipes_title_all)
            )

            Spacer(Modifier.height(16.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(Modifier.clickable { recipesViewModel.showAll() }) {
                    FilterRecipes(
                        text = stringResource(R.string.recipes_filter_all),
                        selected = !uiState.showMine
                    )
                }
                Box(Modifier.clickable { recipesViewModel.showMine() }) {
                    FilterRecipes(
                        text = stringResource(R.string.recipes_filter_mine),
                        selected = uiState.showMine
                    )
                }
            }

            Spacer(Modifier.height(16.dp))

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(18.dp),
                contentPadding = PaddingValues(bottom = 120.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(uiState.visibleRecipes) { rec ->
                    RecipeCard(
                        recipe = rec,
                        onClick = { onRecipeClick(rec.id) }
                    )
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
        Recipes()
    }
}