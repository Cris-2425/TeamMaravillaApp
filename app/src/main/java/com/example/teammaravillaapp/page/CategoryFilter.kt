// com.example.teammaravillaapp.page.CategoryFilter
package com.example.teammaravillaapp.page

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.teammaravillaapp.component.BackButton
import com.example.teammaravillaapp.component.GeneralBackground
import com.example.teammaravillaapp.component.Title
import com.example.teammaravillaapp.data.FakeUserPrefs
import com.example.teammaravillaapp.model.ProductCategory
import com.example.teammaravillaapp.ui.theme.TeamMaravillaAppTheme
import com.example.teammaravillaapp.util.TAG_GLOBAL

@Composable
fun CategoryFilter(
    initialVisibility: Map<ProductCategory, Boolean> = FakeUserPrefs.getCategoryVisibility(),
    onCancel: () -> Unit = {},
    onSave: (Map<ProductCategory, Boolean>) -> Unit = {}
) {
    val categories = ProductCategory.entries
    var toggles by rememberSaveable { mutableStateOf(categories.map { initialVisibility[it] ?: true }) }

    Box(Modifier.fillMaxSize()) {

        // 1) Fondo al fondo del Box
        GeneralBackground() // si quieres menos oscurecido: GeneralBackground(overlayAlpha = 0.15f)

        // 2) Contenido encima
        Column(
            Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextButton(
                    onClick = {
                        Log.e(TAG_GLOBAL, "CategoryFilter → Cancelar")
                        onCancel()
                    }
                ) { Text("Cancelar") }

                Title("Filtro de Categorías")

                TextButton(
                    onClick = {
                        val result = categories.zip(toggles).toMap()
                        FakeUserPrefs.setCategoryVisibility(result)
                        Log.e(TAG_GLOBAL, "CategoryFilter → Guardar: $result")
                        onSave(result)
                    }
                ) { Text("Guardar") }
            }

            Spacer(Modifier.height(12.dp))

            Text("Aspecto de la lista", style = MaterialTheme.typography.titleMedium)

            Spacer(Modifier.height(12.dp))

            categories.forEachIndexed { index, cat ->
                Row(
                    Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(cat.label, style = MaterialTheme.typography.bodyLarge)
                    Switch(
                        checked = toggles[index],
                        onCheckedChange = { checked ->
                            toggles = toggles.toMutableList().also { it[index] = checked }
                            Log.e(TAG_GLOBAL, "CategoryFilter → ${cat.name} = $checked")
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
    TeamMaravillaAppTheme { CategoryFilter() }
}
