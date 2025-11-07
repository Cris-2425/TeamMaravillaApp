package com.example.teammaravillaapp.page

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.teammaravillaapp.component.BackButton
import com.example.teammaravillaapp.component.GeneralBackground
import com.example.teammaravillaapp.ui.theme.TeamMaravillaAppTheme

@Composable
fun CategoryFilter(
    initialVisibility: Map<String, Boolean> = mapOf(
        "Frutas y vegetales" to true,
        "Carne y pescado" to true,
        "Otros" to true,
        "Panadería" to true
    ),
    onCancel: () -> Unit = {},
    onSave: (Map<String, Boolean>) -> Unit = {}
) {

    var panelVisible by remember { mutableStateOf(true) }
    var visibility by remember { mutableStateOf(initialVisibility) }

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

            Row(
                Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {

                TextButton(
                    onClick = onCancel
                ) {
                    Text("Cancelar")
                }
                TextButton(
                    onClick = { onSave(visibility) }
                ) {
                    Text("Guardar")
                }
            }

            Spacer(Modifier.height(8.dp))

            Text(
                "Aspecto de la lista",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier
                    .fillMaxWidth(),
                textAlign = TextAlign.Left
            )

            Spacer(Modifier.height(12.dp))

            // Esta parte la copié porque no sabía como ponerlo
            AnimatedVisibility(visible = panelVisible) {
                Column(
                    Modifier
                        .fillMaxWidth()
                        .clip(MaterialTheme.shapes.medium)
                        .background(MaterialTheme.colorScheme.surfaceVariant)
                        .padding(12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    visibility.forEach { (label, isOn) ->
                        Row(
                            Modifier
                                .fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                label,
                                style = MaterialTheme.typography.bodyLarge
                            )

                            Switch(
                                checked = isOn,
                                onCheckedChange = { checked ->
                                    visibility =
                                        visibility.toMutableMap().apply { this[label] = checked }
                                }
                            )
                        }
                    }
                }
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
fun PreviewCategoryFilter() {
    TeamMaravillaAppTheme {
        CategoryFilter()
    }
}
