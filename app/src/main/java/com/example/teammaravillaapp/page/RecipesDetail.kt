package com.example.teammaravillaapp.page

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.teammaravillaapp.component.BackButton
import com.example.teammaravillaapp.component.GeneralBackground
import com.example.teammaravillaapp.component.ProductBubble
import com.example.teammaravillaapp.model.Recipe
import com.example.teammaravillaapp.model.RecipeData
import com.example.teammaravillaapp.ui.theme.TeamMaravillaAppTheme
import com.example.teammaravillaapp.util.TAG_GLOBAL

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun RecipesDetail(recipe: Recipe) {
    Box(Modifier.fillMaxSize()) {

        GeneralBackground()

        Column(
            Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {

            Spacer(Modifier.height(16.dp))

            Surface(
                shape = RoundedCornerShape(50),
                color = MaterialTheme.colorScheme.secondary,
                contentColor = MaterialTheme.colorScheme.onSecondary
            ) {
                Text(
                    text = recipe.title,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 10.dp),
                    style = MaterialTheme.typography.titleSmall,
                    textAlign = TextAlign.Center
                )
            }

            Spacer(Modifier.height(34.dp))

            if (recipe.imageRes != null) {
                Image(
                    painter = painterResource(recipe.imageRes),
                    contentDescription = "Imagen de receta",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(160.dp)
                        .clip(RoundedCornerShape(20.dp))
                        .clickable {
                            Log.e(TAG_GLOBAL, "RecipesDetail → Imagen pulsada: '${recipe.title}'")
                        },
                    contentScale = ContentScale.Crop
                )
            } else {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(160.dp)
                        .clip(RoundedCornerShape(20.dp))
                        .background(MaterialTheme.colorScheme.secondary),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        "Foto Receta",
                        style = MaterialTheme.typography.titleSmall,
                        color = MaterialTheme.colorScheme.onSecondary
                    )
                }
            }

            Spacer(Modifier.height(34.dp))

            Text(
                text = "Ingredientes",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onBackground,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(34.dp))

            FlowRow(
                maxItemsInEachRow = 3,
                horizontalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterHorizontally),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                recipe.products.forEach {
                    Box(
                        modifier = Modifier.clickable {
                            Log.e(TAG_GLOBAL, "RecipesDetail → Ingrediente pulsado: ${it.name}")
                        }
                    ) {
                        ProductBubble(it)
                    }
                }
            }

            Spacer(Modifier.height(96.dp))
        }

        Box(Modifier.align(Alignment.BottomStart)) {
            BackButton(
                onClick = { Log.e(TAG_GLOBAL, "RecipesDetail → BackButton pulsado") }
            )
        }
    }
}

@Preview
@Composable
fun PreviewRecipesDetail() {
    TeamMaravillaAppTheme {
        RecipesDetail(recipe = RecipeData.recipes.first())
    }
}
