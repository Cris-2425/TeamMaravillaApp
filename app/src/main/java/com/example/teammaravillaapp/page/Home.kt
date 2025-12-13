package com.example.teammaravillaapp.page

import android.app.Application
import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.teammaravillaapp.R
import com.example.teammaravillaapp.component.*
import com.example.teammaravillaapp.data.FakeUserLists
import com.example.teammaravillaapp.model.CardInfo
import com.example.teammaravillaapp.model.OptionButton
import com.example.teammaravillaapp.model.QuickActionData
import com.example.teammaravillaapp.model.SearchFieldData
import com.example.teammaravillaapp.preferences.ThemeMode
import com.example.teammaravillaapp.ui.theme.TeamMaravillaAppTheme
import com.example.teammaravillaapp.util.TAG_GLOBAL
import com.example.teammaravillaapp.viewmodel.AppSettingsViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

/**
 * Pantalla principal (Home / Inicio).
 *
 * @param onNavigateCreateList Navegar a la pantalla de creación de lista.
 * @param onNavigateHome Navegar a Home (BottomBar).
 * @param onNavigateProfile Navegar a Perfil (BottomBar).
 * @param onNavigateCamera Acción al pulsar el botón de "cámara" (BottomBar).
 * @param onNavigateRecipes Navegar a Recetas (BottomBar).
 * @param onExitApp Acción al pulsar "Salir" en el BottomBar o Drawer.
 * @param onOpenList Navegar al detalle de una lista por su id.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Home(
    appSettingsViewModel: AppSettingsViewModel,
    onNavigateCreateList: () -> Unit = {},
    onNavigateHome: () -> Unit = {},
    onNavigateProfile: () -> Unit = {},
    onNavigateCamera: () -> Unit = {},
    onNavigateRecipes: () -> Unit = {},
    onExitApp: () -> Unit = {},
    onOpenList: (String) -> Unit = {},
) {
    val themeMode by appSettingsViewModel.themeMode.collectAsState()

    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    var search by remember { mutableStateOf("") }

    val labelNewList = stringResource(R.string.home_quick_new_list)
    val labelFavorites = stringResource(R.string.home_quick_favorites)
    val labelHistory = stringResource(R.string.home_quick_history)

    val quickActions = listOf(
        QuickActionData(Icons.Default.ShoppingCart, labelNewList),
        QuickActionData(Icons.Default.Favorite, labelFavorites),
        QuickActionData(Icons.Default.Create, labelHistory)
    )

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            DrawerContent(
                onNotifications = {
                    Log.d(TAG_GLOBAL, "Home → Drawer: Notificaciones")
                    scope.launch { drawerState.close() }
                },
                onShare = {
                    Log.d(TAG_GLOBAL, "Home → Drawer: Compartir lista")
                    scope.launch { drawerState.close() }
                },
                onOptions = {
                    Log.d(TAG_GLOBAL, "Home → Drawer: Opciones")
                    scope.launch { drawerState.close() }
                },
                onExit = {
                    Log.d(TAG_GLOBAL, "Home → Drawer: Salir")
                    scope.launch { drawerState.close() }
                    onExitApp()
                }
            )
        }
    ) {
        Scaffold(
            topBar = {
                TopBar(
                    title = stringResource(R.string.app_title),
                    onMenuClick = {
                        Log.d(TAG_GLOBAL, "Home → TopBar: Abrir menú")
                        scope.launch { drawerState.open() }
                    },
                    onSearchClick = {
                        Log.d(TAG_GLOBAL, "Home → TopBar: Buscar")
                    },
                    onMoreClick = {
                        Log.d(TAG_GLOBAL, "Home → TopBar: Más opciones")
                    },
                    isDark = themeMode == ThemeMode.DARK,
                    onThemeToggle = {
                        appSettingsViewModel.setThemeMode(
                            if (themeMode == ThemeMode.DARK)
                                ThemeMode.LIGHT
                            else
                                ThemeMode.DARK
                        )
                    }
                )
            },
            bottomBar = {
                BottomBar(
                    selectedButton = OptionButton.HOME,
                    homeButton = {
                        Log.e(TAG_GLOBAL, "BottomBar → Home")
                        onNavigateHome()
                    },
                    profileButton = {
                        Log.e(TAG_GLOBAL, "BottomBar → Profile")
                        onNavigateProfile()
                    },
                    cameraButton = {
                        Log.e(TAG_GLOBAL, "BottomBar → Camera")
                        onNavigateCamera()
                    },
                    recipesButton = {
                        Log.e(TAG_GLOBAL, "BottomBar → Recipes")
                        onNavigateRecipes()
                    },
                    exitButton = {
                        Log.e(TAG_GLOBAL, "BottomBar → Exit")
                        onExitApp()
                    }
                )
            },
            floatingActionButton = {
                FloatingActionButton(
                    onClick = {
                        Log.d(TAG_GLOBAL, "Home → FAB: Añadir lista")
                        onNavigateCreateList()
                    },
                    containerColor = MaterialTheme.colorScheme.secondary,
                    contentColor = MaterialTheme.colorScheme.onSecondary
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = stringResource(
                            R.string.home_fab_add_content_description
                        )
                    )
                }
            }
        ) { innerPadding ->

            Box(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
            ) {
                GeneralBackground()

                Column(
                    Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp, vertical = 12.dp)
                ) {
                    Title(texto = stringResource(R.string.home_title))

                    Spacer(Modifier.height(16.dp))

                    SearchField(
                        searchData = SearchFieldData(
                            value = search,
                            placeholder = stringResource(R.string.home_search_placeholder)
                        ),
                        onValueChange = {
                            search = it
                            Log.e(TAG_GLOBAL, "Home → SearchField: '$it'")
                        }
                    )

                    Spacer(Modifier.height(20.dp))

                    Text(
                        text = stringResource(R.string.home_quick_actions_title),
                        style = MaterialTheme.typography.titleMedium
                    )

                    Spacer(Modifier.height(8.dp))

                    Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                        quickActions.forEach { qa ->
                            QuickActionButton(
                                quickActionButtonData = qa,
                                onClick = {
                                    Log.d(TAG_GLOBAL, "Home → QuickAction: ${qa.label}")
                                    when (qa.label) {
                                        labelNewList -> onNavigateCreateList()
                                        labelFavorites -> {
                                            // futuro: navegación a Favoritos
                                        }

                                        labelHistory -> {
                                            // futuro: navegación a Historial
                                        }
                                    }
                                }
                            )
                        }
                    }

                    Spacer(Modifier.height(24.dp))

                    Text(
                        text = stringResource(R.string.home_recent_lists_title),
                        style = MaterialTheme.typography.titleMedium
                    )

                    Spacer(Modifier.height(8.dp))

                    // 🔹 Listas recientes dinámicas desde FakeUserLists
                    val recentLists = remember {
                        FakeUserLists.all().ifEmpty {
                            FakeUserLists.sample()      // crea una demo si está vacío
                            FakeUserLists.all()
                        }
                    }

                    if (recentLists.isEmpty()) {
                        Text(
                            text = stringResource(R.string.home_recent_lists_empty),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    } else {
                        Column(
                            verticalArrangement = Arrangement.spacedBy(12.dp),
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                        ) {
                            recentLists.forEach { (id, userList) ->
                                ListCard(
                                    cardInfo = CardInfo(
                                        imageID = R.drawable.list_supermarket, // genérico
                                        imageDescription = userList.name,
                                        title = userList.name,
                                        subtitle = "${userList.products.size} productos"
                                    ),
                                    onClick = { onOpenList(id) }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewHome() {
    var isDark by remember { mutableStateOf(true) }

    TeamMaravillaAppTheme(
        themeMode = if (isDark) ThemeMode.DARK else ThemeMode.LIGHT
    ) {
        Home(
            appSettingsViewModel = object : AppSettingsViewModel(Application()) {
                override val themeMode = MutableStateFlow(
                    if (isDark) ThemeMode.DARK else ThemeMode.LIGHT
                )

                override fun setThemeMode(mode: ThemeMode) {
                    isDark = mode == ThemeMode.DARK
                }
            },
            onNavigateCreateList = {},
            onNavigateHome = {},
            onNavigateProfile = {},
            onNavigateCamera = {},
            onNavigateRecipes = {},
            onExitApp = {},
            onOpenList = {}
        )
    }
}
