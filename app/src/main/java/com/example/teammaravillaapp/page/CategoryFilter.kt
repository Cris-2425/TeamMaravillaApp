package com.example.teammaravillaapp.page

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.teammaravillaapp.R
import com.example.teammaravillaapp.component.BackButton
import com.example.teammaravillaapp.component.GeneralBackground
import com.example.teammaravillaapp.component.Title
import com.example.teammaravillaapp.data.FakeUserPrefs
import com.example.teammaravillaapp.model.ProductCategory
import com.example.teammaravillaapp.ui.theme.TeamMaravillaAppTheme
import com.example.teammaravillaapp.util.TAG_GLOBAL

/**
 * Pantalla de **filtro por categorías**.
 *
 * Muestra una lista de conmutadores (uno por [ProductCategory]) para
 * decidir qué categorías se ven en otras pantallas (por ejemplo, en ListDetail).
 *
 * El estado inicial viene de [FakeUserPrefs.getCategoryVisibility] y al guardar
 * se persiste con [FakeUserPrefs.setCategoryVisibility].
 *
 * @param initialVisibility Mapa opcional con la visibilidad inicial por categoría.
 * @param onCancel Acción al pulsar "Cancelar".
 * @param onSave Callback con el mapa final de visibilidades tras pulsar "Guardar".
 */
@Composable
fun CategoryFilter(
    initialVisibility: Map<ProductCategory, Boolean> = FakeUserPrefs.getCategoryVisibility(),
    onCancel: () -> Unit = {},
    onSave: (Map<ProductCategory, Boolean>) -> Unit = {}
) {
    val categories = ProductCategory.entries

    var toggles by rememberSaveable {
        mutableStateOf(categories.map { initialVisibility[it] ?: true })
    }

    Box(Modifier.fillMaxSize()) {

        GeneralBackground()

        Column(
            Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            // Top bar simple: Cancelar - Título - Guardar
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextButton(
                    onClick = {
                        Log.d(TAG_GLOBAL, "CategoryFilter → Cancelar")
                        onCancel()
                    }
                ) {
                    Text(text = stringResource(R.string.category_filter_cancel))
                }

                Title(texto = stringResource(R.string.category_filter_title))

                TextButton(
                    onClick = {
                        val result = categories.zip(toggles).toMap()
                        FakeUserPrefs.setCategoryVisibility(result)
                        Log.d(TAG_GLOBAL, "CategoryFilter → Guardar: $result")
                        onSave(result)
                    }
                ) {
                    Text(text = stringResource(R.string.category_filter_save))
                }
            }

            Spacer(Modifier.height(12.dp))

            Text(
                text = stringResource(R.string.category_filter_subtitle),
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(Modifier.height(12.dp))

            categories.forEachIndexed { index, cat ->
                Row(
                    Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = stringResource(id = cat.labelRes),
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Switch(
                        checked = toggles[index],
                        onCheckedChange = { checked ->
                            toggles = toggles.toMutableList().also { it[index] = checked }
                            Log.d(TAG_GLOBAL, "CategoryFilter → ${cat.name} = $checked")
                        }
                    )
                }
                Spacer(Modifier.height(8.dp))
            }
        }

        Box(Modifier.align(Alignment.BottomStart)) {
            BackButton(onClick = onCancel)
        }
    }
}

@Preview
@Composable
fun PreviewCategoryFilter() {
    TeamMaravillaAppTheme {
        CategoryFilter()
    }
}