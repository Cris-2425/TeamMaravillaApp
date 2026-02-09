package com.example.teammaravillaapp.page.listdetail.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.teammaravillaapp.R
import com.example.teammaravillaapp.model.ProductCategory
import com.example.teammaravillaapp.ui.theme.TeamMaravillaAppTheme

/**
 * Chip informativo que muestra las categorías activas en el filtro de la pantalla de detalle de lista.
 *
 * - Si no hay categorías seleccionadas, no se renderiza (evita un chip "vacío").
 * - Permite cambiar el filtro o limpiarlo mediante botones.
 *
 * @param selectedCategories Conjunto de categorías seleccionadas actualmente.
 * @param onChange Acción al pulsar "Cambiar" (abre selector de categorías).
 * @param onClear Acción al pulsar "Quitar" (limpia el filtro).
 * @param modifier Modificador de Compose para controlar layout y padding externo.
 *
 * Ejemplo de uso:
 * {@code
 * ActiveFilterChip(
 *   selectedCategories = uiState.selectedCategories,
 *   onChange = viewModel::onOpenCategoryFilter,
 *   onClear = viewModel::onClearCategoryFilter
 * )
 * }
 */
@Composable
internal fun ActiveFilterChip(
    selectedCategories: Set<ProductCategory>,
    onChange: () -> Unit,
    onClear: () -> Unit,
    modifier: Modifier = Modifier
) {
    if (selectedCategories.isEmpty()) return

    Surface(
        shape = MaterialTheme.shapes.large,
        color = MaterialTheme.colorScheme.tertiaryContainer,
        contentColor = MaterialTheme.colorScheme.onTertiaryContainer,
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 10.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            val labels = selectedCategories.map { stringResource(it.labelRes) }

            val text =
                if (labels.size <= 3) {
                    stringResource(R.string.list_detail_filter_chip, labels.joinToString(", "))
                } else {
                    stringResource(
                        R.string.list_detail_filter_chip_more,
                        labels.take(3).joinToString(", "),
                        labels.size - 3
                    )
                }

            Text(
                text = text,
                style = MaterialTheme.typography.titleSmall,
                modifier = Modifier.weight(1f),
                maxLines = 1
            )

            TextButton(
                onClick = onChange,
                contentPadding = PaddingValues(horizontal = 10.dp)
            ) {
                Text(stringResource(R.string.common_change))
            }

            TextButton(
                onClick = onClear,
                contentPadding = PaddingValues(horizontal = 10.dp)
            ) {
                Text(stringResource(R.string.common_remove))
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ActiveFilterChipPreview() {
    TeamMaravillaAppTheme {
        ActiveFilterChip(
            selectedCategories = setOf(ProductCategory.FRUITS, ProductCategory.DAIRY, ProductCategory.MEAT),
            onChange = {},
            onClear = {}
        )
    }
}