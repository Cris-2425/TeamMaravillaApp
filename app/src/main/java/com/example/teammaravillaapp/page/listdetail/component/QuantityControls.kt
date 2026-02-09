package com.example.teammaravillaapp.page.listdetail.component

import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.teammaravillaapp.R
import com.example.teammaravillaapp.ui.theme.TeamMaravillaAppTheme

/**
 * Controles de cantidad: botón de restar, valor y botón de sumar.
 *
 * Componente de presentación: delega las acciones a [onDec] y [onInc].
 *
 * @param quantity Cantidad actual (se muestra como texto).
 * @param onDec Acción al pulsar “-”.
 * @param onInc Acción al pulsar “+”.
 * @param modifier Modificador de Compose para controlar layout.
 *
 * Ejemplo de uso:
 * {@code
 * QuantityControls(
 *   quantity = item.quantity,
 *   onDec = { viewModel.dec(item.productId) },
 *   onInc = { viewModel.inc(item.productId) }
 * )
 * }
 */
@Composable
internal fun QuantityControls(
    quantity: Int,
    onDec: () -> Unit,
    onInc: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = onDec) {
            Icon(
                imageVector = Icons.Default.Remove,
                contentDescription = stringResource(R.string.common_minus)
            )
        }
        Text(
            text = quantity.toString(),
            style = MaterialTheme.typography.titleMedium
        )
        IconButton(onClick = onInc) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = stringResource(R.string.common_plus)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun QuantityControlsPreview() {
    TeamMaravillaAppTheme {
        QuantityControls(
            quantity = 2,
            onDec = {},
            onInc = {}
        )
    }
}