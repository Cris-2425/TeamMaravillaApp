package com.example.teammaravillaapp.component

import android.annotation.SuppressLint
import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.teammaravillaapp.R
import com.example.teammaravillaapp.model.ListBackground
import com.example.teammaravillaapp.ui.theme.TeamMaravillaAppTheme

/**
 * Cuadrícula de selección de fondos para una lista.
 *
 * Muestra 4 opciones (2x2) y notifica la selección al exterior mediante [onSelect].
 * El componente no gestiona estado interno: recibe el fondo seleccionado a través de [selectedBg].
 *
 * @param modifier Modificador para controlar layout, padding y anchura.
 * @param selectedBg Fondo actualmente seleccionado.
 * @param imageResProvider Función que devuelve el recurso drawable asociado a cada [ListBackground].
 * @param onSelect Callback invocado cuando el usuario selecciona un fondo.
 *
 * Ejemplo de uso:
 * {@code
 * BackgroundGrid(
 *   selectedBg = uiState.selectedBackground,
 *   imageResProvider = { bg -> ListBackgrounds.getBackgroundRes(bg) },
 *   onSelect = viewModel::onBackgroundSelected
 * )
 * }
 */
@Composable
fun BackgroundGrid(
    modifier: Modifier = Modifier,
    selectedBg: ListBackground,
    @SuppressLint("SupportAnnotationUsage") @DrawableRes imageResProvider: (ListBackground) -> Int,
    onSelect: (ListBackground) -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        modifier = modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            BackgroundTile(
                selected = selectedBg == ListBackground.FONDO1,
                title = ListBackground.FONDO1.name,
                imageRes = imageResProvider(ListBackground.FONDO1),
                onClick = { onSelect(ListBackground.FONDO1) }
            )
            BackgroundTile(
                selected = selectedBg == ListBackground.FONDO3,
                title = ListBackground.FONDO3.name,
                imageRes = imageResProvider(ListBackground.FONDO3),
                onClick = { onSelect(ListBackground.FONDO3) }
            )
        }

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            BackgroundTile(
                selected = selectedBg == ListBackground.FONDO2,
                title = ListBackground.FONDO2.name,
                imageRes = imageResProvider(ListBackground.FONDO2),
                onClick = { onSelect(ListBackground.FONDO2) }
            )
            BackgroundTile(
                selected = selectedBg == ListBackground.FONDO4,
                title = ListBackground.FONDO4.name,
                imageRes = imageResProvider(ListBackground.FONDO4),
                onClick = { onSelect(ListBackground.FONDO4) }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun BackgroundGridPreview() {
    TeamMaravillaAppTheme {
        BackgroundGrid(
            selectedBg = ListBackground.FONDO1,
            imageResProvider = { R.drawable.list_supermarket },
            onSelect = {}
        )
    }
}