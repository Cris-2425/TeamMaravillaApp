package com.example.teammaravillaapp.component.legacy

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.teammaravillaapp.model.QuickActionData
import com.example.teammaravillaapp.ui.theme.TeamMaravillaAppTheme

/**
 * Botón compacto de acción rápida (icono circular + etiqueta).
 *
 * Componente de presentación: representa una acción “rápida” (por ejemplo, crear lista).
 * El estado habilitado se controla mediante [quickActionButtonData.enabled] y la interacción
 * se delega a [onClick] para mantener la separación de responsabilidades (MVVM).
 *
 * @param quickActionButtonData Datos visuales de la acción (icono, etiqueta y estado enabled).
 * @param modifier Modificador de Compose para controlar tamaño/posición del componente.
 * @param onClick Acción a ejecutar al pulsar (solo se invoca si la acción está habilitada).
 *
 * Ejemplo de uso:
 * {@code
 * QuickActionButton(
 *   quickActionButtonData = QuickActionData(Icons.Default.ShoppingCart, stringResource(R.string.quick_action_new_list)),
 *   onClick = { viewModel.onNewListClick() }
 * )
 * }
 */
@Composable
fun QuickActionButton(
    quickActionButtonData: QuickActionData,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    Column(
        modifier = modifier.width(72.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ElevatedCard(
            modifier = Modifier
                .size(56.dp)
                .clickable(
                    enabled = quickActionButtonData.enabled,
                    onClick = onClick
                )
        ) {
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .background(MaterialTheme.colorScheme.surfaceVariant, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = quickActionButtonData.icon,
                    contentDescription = quickActionButtonData.label
                )
            }
        }

        Spacer(Modifier.height(6.dp))

        Text(
            text = quickActionButtonData.label,
            color = MaterialTheme.colorScheme.primary,
            style = MaterialTheme.typography.labelSmall,
            textAlign = TextAlign.Center,
            maxLines = 1
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun QuickActionButtonPreview() {
    TeamMaravillaAppTheme {
        QuickActionButton(
            quickActionButtonData = QuickActionData(
                icon = Icons.Default.ShoppingCart,
                label = "Nueva lista"
            )
        )
    }
}