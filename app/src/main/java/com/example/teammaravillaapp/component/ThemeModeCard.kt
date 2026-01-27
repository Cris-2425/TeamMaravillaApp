package com.example.teammaravillaapp.component

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.teammaravillaapp.model.ThemeMode

@Composable
fun ThemeModeCard(
    current: ThemeMode,
    onChange: (ThemeMode) -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = MaterialTheme.shapes.large,
        tonalElevation = 2.dp,
        color = MaterialTheme.colorScheme.surfaceContainerLow,
        modifier = modifier.fillMaxWidth()
    ) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Text("Apariencia", style = MaterialTheme.typography.titleMedium)
            Text(
                "Elige cómo quieres el tema de la app.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(Modifier.height(6.dp))

            ThemeModeOption("Sistema", current == ThemeMode.SYSTEM) { onChange(ThemeMode.SYSTEM) }
            ThemeModeOption("Claro", current == ThemeMode.LIGHT) { onChange(ThemeMode.LIGHT) }
            ThemeModeOption("Oscuro", current == ThemeMode.DARK) { onChange(ThemeMode.DARK) }
        }
    }
}

@Composable
private fun ThemeModeOption(
    label: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    Row(
        Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, style = MaterialTheme.typography.bodyLarge)
        RadioButton(selected = selected, onClick = onClick)
    }
}