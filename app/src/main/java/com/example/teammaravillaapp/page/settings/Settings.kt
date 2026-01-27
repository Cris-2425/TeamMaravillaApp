package com.example.teammaravillaapp.page.settings

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.teammaravillaapp.component.BackButton
import com.example.teammaravillaapp.component.GeneralBackground
import com.example.teammaravillaapp.component.Title
import com.example.teammaravillaapp.model.ThemeMode
import com.example.teammaravillaapp.ui.app.ThemeViewModel

@Composable
fun SettingsScreen(
    onBack: () -> Unit,
    themeViewModel: ThemeViewModel
) {
    val mode by themeViewModel.themeMode.collectAsState()

    Box(Modifier.fillMaxSize()) {
        GeneralBackground(overlayAlpha = 0.20f) {

            Column(
                Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp, vertical = 14.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                Title(texto = "Ajustes")

                Surface(
                    shape = MaterialTheme.shapes.extraLarge,
                    tonalElevation = 2.dp
                ) {
                    Column(
                        Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Text(
                            text = "Tema",
                            style = MaterialTheme.typography.titleMedium
                        )
                        Text(
                            text = "Elige si la app sigue al sistema o fuerza claro/oscuro.",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        Spacer(Modifier.height(6.dp))

                        ThemeModeRow(
                            title = "Automático (Sistema)",
                            selected = mode == ThemeMode.SYSTEM,
                            onClick = { themeViewModel.setThemeMode(ThemeMode.SYSTEM) }
                        )
                        ThemeModeRow(
                            title = "Claro",
                            selected = mode == ThemeMode.LIGHT,
                            onClick = { themeViewModel.setThemeMode(ThemeMode.LIGHT) }
                        )
                        ThemeModeRow(
                            title = "Oscuro",
                            selected = mode == ThemeMode.DARK,
                            onClick = { themeViewModel.setThemeMode(ThemeMode.DARK) }
                        )
                    }
                }
            }

            Box(Modifier.align(Alignment.BottomStart)) {
                BackButton(onClick = onBack)
            }
        }
    }
}

@Composable
private fun ThemeModeRow(
    title: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    Surface(
        shape = MaterialTheme.shapes.large,
        color = if (selected) MaterialTheme.colorScheme.primaryContainer
        else MaterialTheme.colorScheme.surfaceContainerLow,
        modifier = Modifier.fillMaxWidth(),
        onClick = onClick
    ) {
        Row(
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            RadioButton(selected = selected, onClick = onClick)
            Spacer(Modifier.width(10.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}