package com.example.teammaravillaapp.page.help.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.teammaravillaapp.ui.theme.TeamMaravillaAppTheme

/**
 * Item de FAQ desplegable (acordeón) con pregunta y respuesta.
 *
 * Al pulsar sobre la pregunta se alterna el estado expandido/colapsado.
 * Se usa [rememberSaveable] para conservar el estado en recomposiciones fuertes
 * (por ejemplo rotación), lo que mejora UX y cumple buenas prácticas.
 *
 * @param question Texto de la pregunta. Restricciones: no nulo, preferiblemente no vacío.
 * @param answer Texto de la respuesta. Restricciones: no nulo (puede ser largo).
 *
 * @throws IllegalStateException No se lanza directamente, pero si se usase dentro de una
 * jerarquía que no permite estado guardable podría requerir ajustes (poco común).
 *
 * Ejemplo de uso:
 * {@code
 * FaqItem(
 *   question = "¿Cómo creo una lista?",
 *   answer = "Pulsa en 'Nueva lista'..."
 * )
 * }
 */
@Composable
internal fun FaqItem(question: String, answer: String) {
    val expanded = rememberSaveable { mutableStateOf(false) }

    Surface(
        shape = MaterialTheme.shapes.large,
        color = MaterialTheme.colorScheme.surfaceContainerLow
    ) {
        Column(
            Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            TextButton(
                onClick = { expanded.value = !expanded.value },
                contentPadding = PaddingValues(0.dp)
            ) {
                Text(question, style = MaterialTheme.typography.titleSmall)
            }

            if (expanded.value) {
                Spacer(Modifier.height(6.dp))
                Text(
                    answer,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}






@Preview(showBackground = true)
@Composable
private fun PreviewFaqItemCollapsed() {
    TeamMaravillaAppTheme {
        FaqItem(question = "¿Cómo funciona?", answer = "Pulsa para desplegar la respuesta.")
    }
}