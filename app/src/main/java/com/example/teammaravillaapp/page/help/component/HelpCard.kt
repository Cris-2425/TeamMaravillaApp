package com.example.teammaravillaapp.page.help.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
 * Tarjeta simple para mostrar un bloque informativo (título + cuerpo).
 *
 * Se utiliza para el bloque “Inicio rápido” en la pantalla de ayuda.
 *
 * @param title Título principal de la tarjeta. Restricciones: no nulo, preferiblemente no vacío.
 * @param body Texto descriptivo. Restricciones: no nulo (puede ser largo).
 *
 * @throws IllegalArgumentException No se lanza directamente, pero se recomienda que el caller
 * no pase cadenas vacías si espera una UI significativa.
 *
 * @see SectionCardHelp Variante de tarjeta para secciones con contenido arbitrario.
 *
 * Ejemplo de uso:
 * {@code
 * HelpCard(
 *   title = "Inicio rápido",
 *   body = "Crea una lista y añade productos..."
 * )
 * }
 */
@Composable
internal fun HelpCard(title: String, body: String) {
    Surface(
        shape = MaterialTheme.shapes.extraLarge,
        tonalElevation = 2.dp,
        color = MaterialTheme.colorScheme.surface
    ) {
        Column(Modifier.fillMaxWidth().padding(14.dp)) {
            Text(title, style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(6.dp))
            Text(
                body,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewHelpCard() {
    TeamMaravillaAppTheme {
        HelpCard(
            title = "Inicio rápido",
            body = "Crea una lista, añade productos y marca lo que ya has comprado."
        )
    }
}