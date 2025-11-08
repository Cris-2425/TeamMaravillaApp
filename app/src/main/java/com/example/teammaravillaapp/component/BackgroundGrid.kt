package com.example.teammaravillaapp.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun BackgroundGrid(
    selectedLabel: String = "Fondo1",
    onSelect: (String) -> Unit = {}
) {

    Row(
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier
            .fillMaxWidth()
    )
    {

        Column(
            Modifier
                .weight(1f),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            BackgroundTile(
                selected = selectedLabel == "Fondo1",
                label = "Fondo1",
                onClick = {
                    onSelect("Fondo1")
                }
            )
            BackgroundTile(
                selected = selectedLabel == "Fondo3",
                label = "Fondo3",
                onClick = {
                    onSelect("Fondo3")
                }
            )
        }
        Column(
            Modifier
                .weight(1f),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            BackgroundTile(
                selected = selectedLabel == "Fondo2",
                label = "Fondo2",
                onClick = {
                    onSelect("Fondo2")
                }
            )
            BackgroundTile(
                selected = selectedLabel == "Fondo4",
                label = "Fondo4",
                onClick = {
                    onSelect("Fondo4")
                }
            )
        }
    }
}