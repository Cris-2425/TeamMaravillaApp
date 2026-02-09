package com.example.teammaravillaapp.component

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.QueryStats
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.teammaravillaapp.R
import com.example.teammaravillaapp.ui.theme.TeamMaravillaAppTheme

/**
 * TopAppBar reutilizable con botón de menú, búsqueda y menú de “más opciones”.
 *
 * Componente de presentación: delega las acciones al exterior mediante callbacks.
 * Mantiene únicamente estado de UI interno para controlar el desplegable del menú.
 *
 * @param modifier Modificador de Compose para controlar layout.
 * @param title Título mostrado en la barra.
 * @param showLogo Si es `true`, muestra el logo junto al título (usando [Title]).
 * @param onMenuClick Acción al pulsar el icono de menú.
 * @param onSearchClick Acción al pulsar el icono de búsqueda.
 * @param onOpenSettings Acción al seleccionar “Ajustes”.
 * @param onOpenHelp Acción al seleccionar “Ayuda”.
 * @param onOpenStats Acción al seleccionar “Estadísticas”.
 *
 * Ejemplo de uso:
 * {@code
 * TopBar(
 *   title = stringResource(R.string.app_title),
 *   showLogo = true,
 *   onMenuClick = { drawerState.open() },
 *   onSearchClick = { navController.navigate(Screen.Search.route) }
 * )
 * }
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
    modifier: Modifier = Modifier,
    title: String,
    showLogo: Boolean = false,
    onMenuClick: () -> Unit = {},
    onSearchClick: () -> Unit = {},
    onOpenSettings: () -> Unit = {},
    onOpenHelp: () -> Unit = {},
    onOpenStats: () -> Unit = {}
) {
    var moreExpanded by remember { mutableStateOf(false) }

    CenterAlignedTopAppBar(
        modifier = modifier,
        title = { Title(texto = title, showLogo = showLogo) },
        navigationIcon = {
            IconButton(onClick = onMenuClick) {
                Icon(
                    imageVector = Icons.Default.Menu,
                    contentDescription = stringResource(R.string.topbar_menu_cd)
                )
            }
        },
        actions = {
            IconButton(onClick = onSearchClick) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = stringResource(R.string.topbar_search_cd)
                )
            }

            IconButton(onClick = { moreExpanded = true }) {
                Icon(
                    imageVector = Icons.Default.MoreVert,
                    contentDescription = stringResource(R.string.topbar_more_cd)
                )
            }

            DropdownMenu(
                expanded = moreExpanded,
                onDismissRequest = { moreExpanded = false }
            ) {
                DropdownMenuItem(
                    text = { Text(stringResource(R.string.topbar_menu_settings)) },
                    leadingIcon = { Icon(Icons.Default.Settings, contentDescription = null) },
                    onClick = {
                        moreExpanded = false
                        onOpenSettings()
                    }
                )

                DropdownMenuItem(
                    text = { Text(stringResource(R.string.topbar_menu_stats)) },
                    leadingIcon = { Icon(Icons.Default.QueryStats, contentDescription = null) },
                    onClick = {
                        moreExpanded = false
                        onOpenStats()
                    }
                )

                DropdownMenuItem(
                    text = { Text(stringResource(R.string.topbar_menu_help)) },
                    leadingIcon = { Icon(Icons.Default.HelpOutline, contentDescription = null) },
                    onClick = {
                        moreExpanded = false
                        onOpenHelp()
                    }
                )
            }
        },
        colors = androidx.compose.material3.TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    )
}

@Preview(showBackground = true)
@Composable
private fun TopBarPreview() {
    TeamMaravillaAppTheme {
        TopBar(
            title = "Team Maravilla",
            showLogo = true
        )
    }
}