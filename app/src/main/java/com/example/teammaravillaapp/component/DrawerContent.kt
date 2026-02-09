package com.example.teammaravillaapp.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.teammaravillaapp.R
import com.example.teammaravillaapp.ui.theme.TeamMaravillaAppTheme

/**
 * Contenido del menú lateral (drawer).
 *
 * Muestra un conjunto de opciones (notificaciones, compartir, opciones y salir) y delega
 * la interacción al exterior mediante callbacks. Este componente no contiene lógica de negocio.
 *
 * @param modifier Modificador de Compose para controlar layout, tamaño o padding del contenedor.
 * @param onNotifications Acción al pulsar “Notificaciones”.
 * @param onShare Acción al pulsar “Compartir lista”.
 * @param onOptions Acción al pulsar “Opciones”.
 * @param onExit Acción al pulsar “Salir”.
 *
 * Ejemplo de uso:
 * {@code
 * DrawerContent(
 *   onNotifications = { viewModel.onNotificationsClick() },
 *   onShare = { viewModel.onShareClick() },
 *   onOptions = { navController.navigate(Screen.Options.route) },
 *   onExit = { viewModel.onExitClick() }
 * )
 * }
 */
@Composable
fun DrawerContent(
    modifier: Modifier = Modifier,
    onNotifications: () -> Unit,
    onShare: () -> Unit,
    onOptions: () -> Unit,
    onExit: () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.secondary)
            .padding(vertical = 8.dp)
    ) {
        Spacer(Modifier.height(12.dp))

        DrawerItem(
            text = stringResource(R.string.drawer_notifications),
            onClick = onNotifications
        )

        DrawerItem(
            text = stringResource(R.string.drawer_share_list),
            onClick = onShare
        )

        DrawerItem(
            text = stringResource(R.string.drawer_options),
            onClick = onOptions
        )

        DrawerItem(
            text = stringResource(R.string.drawer_exit),
            onClick = onExit
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun DrawerContentPreview() {
    TeamMaravillaAppTheme {
        DrawerContent(
            onNotifications = {},
            onShare = {},
            onOptions = {},
            onExit = {}
        )
    }
}