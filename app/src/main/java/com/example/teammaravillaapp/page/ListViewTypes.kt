package com.example.teammaravillaapp.page

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.teammaravillaapp.component.BackButton
import com.example.teammaravillaapp.component.CircularOption
import com.example.teammaravillaapp.component.GeneralBackground
import com.example.teammaravillaapp.model.ListStyle
import com.example.teammaravillaapp.ui.theme.TeamMaravillaAppTheme

@Composable
fun ListViewTypes(
    selected: ListStyle = ListStyle.LISTA,
    onSelect: (ListStyle) -> Unit = {},
    onCancel: () -> Unit = {},
    onSave: () -> Unit = {}
) {
    var current by remember { mutableStateOf(selected) }

    Box(
        Modifier
            .fillMaxSize()
    ) {

        GeneralBackground()

        Column(
            Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {

            Row(Modifier
                .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {

                TextButton(
                    onClick = onCancel
                ) {
                    Text("Cancelar")
                }

                TextButton(
                    onClick = {
                    onSelect(current)
                    onSave()
                }
                ) {
                    Text("Guardar")
                }
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
                    }
                )

                CircularOption(
                    label = "Mosaic",
                    selected = current == ListStyle.MOSAIC,
                    onClick = {
                        current = ListStyle.MOSAIC
                    }
                )

                CircularOption(
                    label = "Etc",
                    selected = current == ListStyle.ETC,
                    onClick = {
                        current = ListStyle.ETC
                    }
                )
            }
        }

        Box(
            modifier = Modifier
                .align(Alignment.BottomStart)
        ) {

            BackButton()
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
