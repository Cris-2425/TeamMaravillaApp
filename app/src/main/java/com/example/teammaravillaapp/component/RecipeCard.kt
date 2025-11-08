package com.example.teammaravillaapp.component

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.teammaravillaapp.model.Recipe
import com.example.teammaravillaapp.model.RecipeData
import com.example.teammaravillaapp.ui.theme.TeamMaravillaAppTheme
import com.example.teammaravillaapp.util.TAG_GLOBAL
import com.example.teammaravillaapp.data.FakeUserRecipes

/**
 * Tarjeta de receta reutilizable.
 *
 * Muestra:
 * - Cabecera con el **título** y un **botón de favorito** (Mis Recetas).
 * - Imagen (o placeholder si no hay imagen).
 * - Sección **"Ingredientes"** con los productos como burbujas.
 *
 * Acciones:
 * - **Tap** sobre la tarjeta → [onClick].
 * - **Tap** sobre el corazón → añade/quita de *Mis Recetas* en [FakeUserRecipes].
 *
 * Notas de estado:
 * - `isMine` se inicializa leyendo el repositorio en memoria. No persiste entre sesiones (MVP).
 *
 * @param recipe Receta a renderizar.
 * @param modifier Modificador de Jetpack Compose para ajustar tamaño/espaciados/… desde fuera.
 * @param onClick Acción al pulsar la tarjeta completa (navegar a detalle, etc.).
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun RecipeCard(
    recipe: Recipe,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    /**
     * Estado local que refleja si la receta está en "Mis Recetas".
     * Se recalcula cuando cambia el título de la receta.
     */
    val isMine = remember(recipe.title) { mutableStateOf(FakeUserRecipes.contains(recipe)) }

    Surface(
        shape = RoundedCornerShape(22.dp),
        color = MaterialTheme.colorScheme.secondary,
        contentColor = MaterialTheme.colorScheme.onSecondary,
        modifier = modifier
            .fillMaxWidth()
            .clickable {
                Log.e(TAG_GLOBAL, "Click en '${recipe.title}'")
                onClick()
            }
    ) {
        Column(Modifier.padding(14.dp)) {

            /** Cabecera con título e icono de favorito */
            Surface(
                shape = RoundedCornerShape(50),
                color = MaterialTheme.colorScheme.surface,
                contentColor = MaterialTheme.colorScheme.onSurface
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 8.dp)
                ) {
                    Text(
                        text = recipe.title,
                        style = MaterialTheme.typography.titleSmall,
                        modifier = Modifier
                            .weight(1f)
                            .padding(start = 8.dp)
                            .align(Alignment.CenterVertically),
                        textAlign = TextAlign.Center
                    )

                    /** Botón de favorito: añade o elimina del repositorio en memoria */
                    IconButton(onClick = {
                        if (isMine.value) {
                            FakeUserRecipes.remove(recipe)
                        } else {
                            FakeUserRecipes.add(recipe)
                        }
                        isMine.value = !isMine.value
                    }) {
                        Icon(
                            imageVector = if (isMine.value)
                                Icons.Filled.Favorite
                            else
                                Icons.Outlined.FavoriteBorder,
                            contentDescription = if (isMine.value)
                                "Quitar de Mis Recetas"
                            else
                                "Añadir a Mis Recetas",
                            tint = if (isMine.value)
                                MaterialTheme.colorScheme.primary
                            else
                                MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }

            Spacer(Modifier.height(12.dp))

            if (recipe.imageRes != null) {
                Image(
                    painter = painterResource(id = recipe.imageRes),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(110.dp)
                        .clip(RoundedCornerShape(14.dp)),
                    contentScale = ContentScale.Crop
                )
            } else {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(110.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .background(MaterialTheme.colorScheme.secondaryContainer),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        "Foto Receta",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.onSecondaryContainer
                    )
                }
            }

            Spacer(Modifier.height(12.dp))

            Text(
                text = "Ingredientes",
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.onSecondary
            )

            Spacer(Modifier.height(6.dp))

            if (recipe.products.isEmpty()) {
                Text(
                    text = "Sin ingredientes",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSecondary
                )
            } else {
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    recipe.products.forEach {
                        ProductBubble(it)
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun PreviewRecipeCard() {
    TeamMaravillaAppTheme {
        RecipeCard(RecipeData.recipes.first())
    }
}