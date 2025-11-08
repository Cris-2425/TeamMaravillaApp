package com.example.teammaravillaapp.page

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.teammaravillaapp.component.BackButton
import com.example.teammaravillaapp.component.CircularOption
import com.example.teammaravillaapp.component.GeneralBackground
import com.example.teammaravillaapp.component.Title
import com.example.teammaravillaapp.model.ListStyle
import com.example.teammaravillaapp.ui.theme.TeamMaravillaAppTheme
import com.example.teammaravillaapp.util.TAG_GLOBAL

/**
 * # Pantalla: **Tipos de vista de lista**
 *
 * Permite al usuario elegir el estilo visual preferido para mostrar sus listas.
 * Los tres estilos disponibles son:
 * - [ListStyle.LISTA] → formato clásico vertical.
 * - [ListStyle.MOSAIC] → formato cuadrícula.
 * - [ListStyle.ETC] → experimental o alternativo.
 *
 * @param selected Estilo actualmente seleccionado.
 * @param onSelect Callback con el estilo seleccionado.
 * @param onCancel Acción al pulsar “Cancelar”.
 * @param onSave Acción al pulsar “Guardar”.
 */
@Composable
fun ListViewTypes(
    selected: ListStyle = ListStyle.LISTA,
    onSelect: (ListStyle) -> Unit = {},
    onCancel: () -> Unit = {},
    onSave: () -> Unit = {}
) {
    var current by rememberSaveable { mutableStateOf(selected) }

    Box(Modifier.fillMaxSize()) {
        GeneralBackground(overlayAlpha = 0.15f)

        Column(
            Modifier.fillMaxSize().padding(16.dp)
        ) {
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextButton(onClick = { onCancel() }) { Text("Cancelar") }
                Title("Filtrar Aspecto")
                TextButton(onClick = { onSelect(current); onSave() }) { Text("Guardar") }
            }

            Spacer(Modifier.height(24.dp))
            Text(
                "Aspecto de la lista",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
            Spacer(Modifier.height(24.dp))

            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                CircularOption("Lista", current == ListStyle.LISTA) {
                    current = ListStyle.LISTA
                }
                CircularOption("Mosaic", current == ListStyle.MOSAIC) {
                    current = ListStyle.MOSAIC
                }
                CircularOption("Etc", current == ListStyle.ETC) {
                    current = ListStyle.ETC
                }
            }
        }

        Box(Modifier.align(Alignment.BottomStart)) {
            BackButton(onClick = onCancel)
        }
    }
}

@Preview
@Composable
fun PreviewListViewTypes() {
    TeamMaravillaAppTheme {
        ListViewTypes()
    }
}