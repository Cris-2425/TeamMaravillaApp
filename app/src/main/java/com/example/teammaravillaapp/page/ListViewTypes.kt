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
// import com.example.teammaravillaapp.data.FakeUserPrefs // si lo tienes

@Composable
fun ListViewTypes(
    selected: ListStyle = ListStyle.LISTA,
    onSelect: (ListStyle) -> Unit = {},
    onCancel: () -> Unit = {},
    onSave: () -> Unit = {}
) {
    var current by rememberSaveable { mutableStateOf(selected) }

    Box(Modifier.fillMaxSize()) {

        // Fondo detrás
        GeneralBackground(overlayAlpha = 0.15f)

        // Contenido encima
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
                        Log.e(TAG_GLOBAL, "ListViewTypes → Cancelar (estilo actual: $current)")
                        onCancel()
                    }
                ) { Text("Cancelar") }

                Title("Filtrar Aspecto")

                TextButton(
                    onClick = {
                        Log.e(TAG_GLOBAL, "ListViewTypes → Guardar: $current")
                        // FakeUserPrefs.setListStyle(current)
                        onSelect(current)
                        onSave()
                    }
                ) { Text("Guardar") }
            }

            Spacer(Modifier.height(12.dp))

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
                CircularOption(
                    label = "Lista",
                    selected = current == ListStyle.LISTA,
                    onClick = {
                        current = ListStyle.LISTA
                        Log.e(TAG_GLOBAL, "ListViewTypes → Seleccionado LISTA")
                    }
                )
                CircularOption(
                    label = "Mosaic",
                    selected = current == ListStyle.MOSAIC,
                    onClick = {
                        current = ListStyle.MOSAIC
                        Log.e(TAG_GLOBAL, "ListViewTypes → Seleccionado MOSAIC")
                    }
                )
                CircularOption(
                    label = "Etc",
                    selected = current == ListStyle.ETC,
                    onClick = {
                        current = ListStyle.ETC
                        Log.e(TAG_GLOBAL, "ListViewTypes → Seleccionado ETC")
                    }
                )
            }
        }

        Box(Modifier.align(Alignment.BottomStart)) {
            BackButton {
                Log.e(TAG_GLOBAL, "ListViewTypes → BackButton")
                onCancel()
            }
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
