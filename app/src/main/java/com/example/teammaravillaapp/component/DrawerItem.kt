package com.example.teammaravillaapp.component

import android.content.res.Configuration
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.teammaravillaapp.ui.theme.TeamMaravillaAppTheme

/**
 * Elemento individual del menú lateral (drawer).
 *
 * Representa una opción textual clicable dentro de un menú lateral.
 * No contiene lógica de negocio ni navegación, delegando la acción en [onClick].
 *
 * @param modifier Modificador de Compose para controlar layout, padding o accesibilidad.
 * @param text Texto mostrado en la opción.
 * @param onClick Acción a ejecutar al pulsar el elemento.
 *
 * @see DrawerContent
 *
 * Ejemplo de uso:
 * {@code
 * DrawerItem(
 *   text = stringResource(R.string.drawer_options),
 *   onClick = { viewModel.onOptionsClick() }
 * )
 * }
 */
@Composable
fun DrawerItem(
    modifier: Modifier = Modifier,
    text: String,
    onClick: () -> Unit
) {
    Text(
        text = text,
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(16.dp),
        color = MaterialTheme.colorScheme.onTertiaryContainer
    )
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun DrawerItemPreview() {
    TeamMaravillaAppTheme {
        DrawerItem(
            text = "Drawer Item Preview",
            onClick = {}
        )
    }
}