package com.example.teammaravillaapp.page.help.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.teammaravillaapp.ui.theme.TeamMaravillaAppTheme

/**
 * Contenedor visual para una sección de ayuda (título + separador + contenido).
 *
 * Motivo: mantener consistencia de diseño (Material 3) en las secciones como FAQ y Contacto.
 *
 * @param title Título visible de la sección. Restricciones: no nulo, preferiblemente corto.
 * @param content Contenido composable dentro de la sección.
 * Restricciones:
 * - No debe lanzar excepciones.
 * - Debe respetar el ancho disponible (fillMaxWidth recomendado).
 *
 * @see HelpCard Tarjeta simple “título + cuerpo”.
 *
 * Ejemplo de uso:
 * {@code
 * SectionCardHelp(title = "FAQ") {
 *   FaqItem(question = "...", answer = "...")
 * }
 * }
 */
@Composable
internal fun SectionCardHelp(
    title: String,
    content: @Composable ColumnScope.() -> Unit
) {
    Surface(
        shape = MaterialTheme.shapes.extraLarge,
        tonalElevation = 2.dp,
        color = MaterialTheme.colorScheme.surface
    ) {
        Column(
            Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text(title, style = MaterialTheme.typography.titleMedium)
            Divider()
            content()
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewSectionCardHelp() {
    TeamMaravillaAppTheme {
        SectionCardHelp(title = "FAQ") {
            FaqItem(question = "¿Ejemplo?", answer = "Respuesta de ejemplo.")
        }
    }
}