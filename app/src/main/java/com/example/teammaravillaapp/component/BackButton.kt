package com.example.teammaravillaapp.component

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.teammaravillaapp.R
import com.example.teammaravillaapp.ui.theme.TeamMaravillaAppTheme

/**
 * Botón flotante para volver a la pantalla anterior.
 *
 * Este componente representa una acción de navegación “volver atrás” de forma consistente
 * con Material Design 3. No contiene lógica de navegación interna: delega la acción en
 * el callback [onClick] para mantener la UI como presentación pura.
 *
 * @param modifier Modificador de Compose para personalizar posición, padding, tamaño o accesibilidad.
 * @param onClick Acción a ejecutar al pulsar el botón (por ejemplo, `navController.navigateUp()`).
 *
 * @see FloatingActionButton
 *
 * Ejemplo de uso:
 * {@code
 * BackButton(
 *   modifier = Modifier.padding(start = 16.dp, bottom = 16.dp),
 *   onClick = { navController.navigateUp() }
 * )
 * }
 */
@Composable
fun BackButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    FloatingActionButton(
        onClick = onClick,
        containerColor = MaterialTheme.colorScheme.secondary,
        contentColor = MaterialTheme.colorScheme.onSecondary,
        modifier = modifier
    ) {
        Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
            contentDescription = stringResource(R.string.back_button_content_description)
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun BackButtonPreview() {
    TeamMaravillaAppTheme {
        BackButton(onClick = {})
    }
}