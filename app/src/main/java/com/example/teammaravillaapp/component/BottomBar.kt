package com.example.teammaravillaapp.component

import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.teammaravillaapp.R
import com.example.teammaravillaapp.model.OptionButton
import com.example.teammaravillaapp.ui.theme.TeamMaravillaAppTheme

/**
 * Barra de navegación inferior principal de la aplicación.
 *
 * Renderiza una [NavigationBar] con las secciones principales (Home, Recetas, Historial, Perfil),
 * marcando como seleccionada la opción indicada por [selectedButton]. Este componente es
 * puramente de presentación: delega la navegación a los callbacks de click.
 *
 * @param modifier Modificador de Compose para personalizar el layout (anchura, padding, etc.).
 * @param selectedButton Opción actualmente seleccionada para reflejar el estado de navegación.
 * @param onHome Acción al pulsar la opción Home.
 * @param onRecipes Acción al pulsar la opción Recetas.
 * @param onHistory Acción al pulsar la opción Historial.
 * @param onProfile Acción al pulsar la opción Perfil.
 *
 * @see NavigationBar
 * @see NavigationBarItem
 *
 * Ejemplo de uso:
 * {@code
 * BottomBar(
 *   selectedButton = uiState.selectedTab,
 *   onHome = { navController.navigate(Screen.Home.route) },
 *   onRecipes = { navController.navigate(Screen.Recipes.route) },
 *   onHistory = { navController.navigate(Screen.History.route) },
 *   onProfile = { navController.navigate(Screen.Profile.route) }
 * )
 * }
 */
@Composable
fun BottomBar(
    modifier: Modifier = Modifier,
    selectedButton: OptionButton,
    onHome: () -> Unit,
    onRecipes: () -> Unit,
    onHistory: () -> Unit,
    onProfile: () -> Unit
) {
    NavigationBar(modifier = modifier) {
        NavigationBarItem(
            selected = selectedButton == OptionButton.HOME,
            onClick = onHome,
            icon = {
                Icon(
                    imageVector = Icons.Default.Home,
                    contentDescription = stringResource(R.string.bottom_bar_home_cd),
                    modifier = Modifier.size(26.dp)
                )
            },
            label = { Text(stringResource(R.string.bottom_bar_home_label)) }
        )

        NavigationBarItem(
            selected = selectedButton == OptionButton.RECIPES,
            onClick = onRecipes,
            icon = {
                Icon(
                    imageVector = Icons.Default.Restaurant,
                    contentDescription = stringResource(R.string.bottom_bar_recipes_cd),
                    modifier = Modifier.size(26.dp)
                )
            },
            label = { Text(stringResource(R.string.bottom_bar_recipes_label)) }
        )

        NavigationBarItem(
            selected = selectedButton == OptionButton.HISTORY,
            onClick = onHistory,
            icon = {
                Icon(
                    imageVector = Icons.Default.History,
                    contentDescription = stringResource(R.string.bottom_bar_history_cd),
                    modifier = Modifier.size(26.dp)
                )
            },
            label = { Text(stringResource(R.string.bottom_bar_history_label)) }
        )

        NavigationBarItem(
            selected = selectedButton == OptionButton.PROFILE,
            onClick = onProfile,
            icon = {
                Icon(
                    imageVector = Icons.Default.AccountCircle,
                    contentDescription = stringResource(R.string.bottom_bar_profile_cd),
                    modifier = Modifier.size(26.dp)
                )
            },
            label = { Text(stringResource(R.string.bottom_bar_profile_label)) }
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun BottomBarPreview() {
    TeamMaravillaAppTheme {
        BottomBar(
            selectedButton = OptionButton.HOME,
            onHome = {},
            onRecipes = {},
            onHistory = {},
            onProfile = {}
        )
    }
}