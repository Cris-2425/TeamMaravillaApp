package com.example.teammaravillaapp.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.teammaravillaapp.ui.theme.TeamMaravillaAppTheme

/**
 * Burbuja circular para mostrar una opción rápida (por ejemplo, en la pantalla de perfil).
 *
 * Es un componente de presentación: no incorpora lógica de click por sí mismo.
 * Si se necesita interacción, se recomienda envolverlo con `Modifier.clickable { ... }`.
 *
 * @param modifier Modificador de Compose para controlar tamaño, padding y comportamiento de layout.
 * @param label Texto mostrado dentro de la burbuja. Recomendado: corto (1–2 palabras).
 *
 * Ejemplo de uso:
 * {@code
 * OptionBubble(
 *   label = stringResource(R.string.profile_my_lists),
 *   modifier = Modifier.clickable { viewModel.onMyListsClick() }
 * )
 * }
 */
@Composable
fun OptionBubble(
    modifier: Modifier = Modifier,
    label: String
) {
    Surface(
        modifier = modifier.size(90.dp),
        shape = CircleShape,
        color = MaterialTheme.colorScheme.tertiaryContainer,
        contentColor = MaterialTheme.colorScheme.onTertiaryContainer
    ) {
        Box(contentAlignment = Alignment.Center) {
            Text(
                text = label,
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun OptionBubblePreview() {
    TeamMaravillaAppTheme {
        OptionBubble(label = "Mis listas")
    }
}