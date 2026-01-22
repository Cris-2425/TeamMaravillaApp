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
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.teammaravillaapp.R
import com.example.teammaravillaapp.component.BackButton
import com.example.teammaravillaapp.component.GeneralBackground
import com.example.teammaravillaapp.component.Title
import com.example.teammaravillaapp.model.ProductCategory
import com.example.teammaravillaapp.page.prefs.UserPrefsViewModel
import com.example.teammaravillaapp.ui.theme.TeamMaravillaAppTheme
import com.example.teammaravillaapp.util.TAG_GLOBAL

@Composable
fun CategoryFilter(
    onCancel: () -> Unit = {},
    onSave: (Map<ProductCategory, Boolean>) -> Unit = {},
    vm: UserPrefsViewModel = hiltViewModel()
) {
    val categories = ProductCategory.entries
    val prefsState by vm.uiState.collectAsState()

    var toggles by rememberSaveable { mutableStateOf<List<Boolean>>(emptyList()) }

    // Inicializa toggles cuando llega el map del repo
    LaunchedEffect(prefsState.categoryVisibility) {
        toggles = categories.map { prefsState.categoryVisibility[it] ?: true }
    }

    val ready = toggles.size == categories.size

    Box(Modifier.fillMaxSize()) {
        GeneralBackground()

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
                        Log.d(TAG_GLOBAL, "CategoryFilter → Cancelar")
                        onCancel()
                    }
                ) { Text(text = stringResource(R.string.category_filter_cancel)) }

                Title(texto = stringResource(R.string.category_filter_title))

                TextButton(
                    enabled = ready,
                    onClick = {
                        // ✅ visibilityMap (para devolver al caller si quieres)
                        val visibilityMap = categories.zip(toggles).toMap()

                        // ✅ hiddenSet (lo que TU repo guarda)
                        val hiddenSet: Set<ProductCategory> =
                            categories.zip(toggles)
                                .filter { (_, visible) -> !visible }
                                .map { (cat, _) -> cat }
                                .toSet()

                        vm.setHiddenCategories(hiddenSet)

                        Log.d(TAG_GLOBAL, "CategoryFilter → Guardar hidden=$hiddenSet")
                        onSave(visibilityMap)
                    }
                ) { Text(text = stringResource(R.string.category_filter_save)) }
            }

            Spacer(Modifier.height(12.dp))

            Text(
                text = stringResource(R.string.category_filter_subtitle),
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(Modifier.height(12.dp))

            categories.forEachIndexed { index, cat ->
                val checked = if (ready) toggles[index] else true

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
                        checked = checked,
                        onCheckedChange = { newChecked ->
                            if (!ready) return@Switch
                            toggles = toggles.toMutableList().also { it[index] = newChecked }
                            Log.d(TAG_GLOBAL, "CategoryFilter → ${cat.name} = $newChecked")
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