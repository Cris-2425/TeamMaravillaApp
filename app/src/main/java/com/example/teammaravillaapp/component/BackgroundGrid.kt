package com.example.teammaravillaapp.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.teammaravillaapp.model.ListBackground

@Composable
fun BackgroundGrid(
    selectedBg: ListBackground = ListBackground.FONDO1,
    onSelect: (ListBackground) -> Unit = {}
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
                selected = selectedBg == ListBackground.FONDO1,
                bg = ListBackground.FONDO1,
                onClick = {
                    onSelect(ListBackground.FONDO1)
                }
            )
            BackgroundTile(
                selected = selectedBg == ListBackground.FONDO3,
                bg = ListBackground.FONDO3,
                onClick = {
                    onSelect(ListBackground.FONDO3)
                }
            )
        }
        Column(
            Modifier
                .weight(1f),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            BackgroundTile(
                selected = selectedBg == ListBackground.FONDO2,
                bg = ListBackground.FONDO2,
                onClick = {
                    onSelect(ListBackground.FONDO2)
                }
            )
            BackgroundTile(
                selected = selectedBg == ListBackground.FONDO4,
                bg = ListBackground.FONDO4,
                onClick = {
                    onSelect(ListBackground.FONDO4)
                }
            )
        }
    }
}