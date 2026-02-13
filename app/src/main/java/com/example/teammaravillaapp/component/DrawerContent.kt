package com.example.teammaravillaapp.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
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
 * Muestra un saludo al usuario y accesos directos a secciones frecuentes.
 * Este componente no contiene lógica de negocio: delega la interacción al exterior mediante callbacks.
 *
 * @param username Nombre a mostrar en el saludo. Si está vacío, se usa un fallback.
 * @param modifier Modificador de Compose para controlar layout, tamaño o padding del contenedor.
 * @param onMyRecipes Acción al pulsar “Mis recetas”.
 * @param onShare Acción al pulsar “Compartir lista”.
 * @param onSettings Acción al pulsar “Ajustes”.
 * @param onLogout Acción al pulsar “Cerrar sesión”.
 */
@Composable
fun DrawerContent(
    username: String?,
    modifier: Modifier = Modifier,
    onMyRecipes: () -> Unit,
    onShare: () -> Unit,
    onSettings: () -> Unit,
    onLogout: () -> Unit
) {
    val safeName = username
        ?.takeIf(String::isNotBlank)
        ?: stringResource(R.string.drawer_username_fallback)

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.secondaryContainer)
            .padding(vertical = 8.dp)
    ) {
        Spacer(Modifier.height(12.dp))

        Text(
            text = stringResource(R.string.drawer_hello_username, safeName),
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSecondaryContainer,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp)
        )

        Spacer(Modifier.height(8.dp))

        DrawerItem(
            text = stringResource(R.string.drawer_my_recipes),
            onClick = onMyRecipes,
        )

        DrawerItem(
            text = stringResource(R.string.drawer_share_list),
            onClick = onShare
        )

        DrawerItem(
            text = stringResource(R.string.drawer_settings),
            onClick = onSettings
        )

        DrawerItem(
            text = stringResource(R.string.drawer_logout),
            onClick = onLogout
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun DrawerContentPreview() {
    TeamMaravillaAppTheme {
        DrawerContent(
            username = "Cristian",
            onMyRecipes = {},
            onShare = {},
            onSettings = {},
            onLogout = {}
        )
    }
}