package com.example.teammaravillaapp.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.teammaravillaapp.ui.theme.TeamMaravillaAppTheme

/**
 * Contenedor reutilizable para secciones en formato tarjeta.
 *
 * Aplica un estilo consistente (shape, elevación y color de contenedor) para agrupar contenido
 * relacionado dentro de una pantalla.
 *
 * @param modifier Modificador de Compose para controlar layout, padding y tamaño.
 * @param content Contenido a renderizar dentro de la sección. Se ejecuta en un [ColumnScope].
 *
 * Ejemplo de uso:
 * {@code
 * SectionCard {
 *   Text(text = stringResource(R.string.profile_section_title))
 *   Spacer(Modifier.height(8.dp))
 *   ...
 * }
 * }
 */
@Composable
fun SectionCard(
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.large,
        tonalElevation = 2.dp,
        color = MaterialTheme.colorScheme.surfaceContainerLow
    ) {
        Column(
            Modifier.padding(14.dp),
            content = content
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun SectionCardPreview() {
    TeamMaravillaAppTheme {
        SectionCard {
            Text("Título de sección")
            Text("Contenido de ejemplo")
        }
    }
}