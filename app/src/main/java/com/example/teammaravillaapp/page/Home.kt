package com.example.teammaravillaapp.page

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
import com.example.teammaravillaapp.model.CardInfo
import com.example.teammaravillaapp.model.QuickActionData
import com.example.teammaravillaapp.model.SearchFieldData
import com.example.teammaravillaapp.model.OptionButton
import com.example.teammaravillaapp.ui.theme.TeamMaravillaAppTheme
import com.example.teammaravillaapp.util.TAG_GLOBAL
import kotlinx.coroutines.launch

/**
 * Pantalla **Home**.
 *
 * Estructura base:
 * - Drawer lateral con [DrawerContent] para acciones globales.
 * - Scaffold con [TopBar], [BottomBar] y FAB.
 * - Buscador, acciones rápidas y un bloque de “listas recientes”.
 *
 * @param onNavigateCreateList Acción al crear una nueva lista (FAB o acción rápida).
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Home(
    onNavigateCreateList: () -> Unit = {}
) {
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
                    }
                )
            },
            bottomBar = {
                BottomBar(selectedButton = OptionButton.HOME)
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
                        contentDescription = stringResource(R.string.home_fab_add_content_description)
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
                            Log.d(TAG_GLOBAL, "Home → SearchField: '$it'")
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

                    Column(
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                    ) {
                        ListCard(
                            cardInfo = CardInfo(
                                imageID = R.drawable.list_supermarket,
                                imageDescription = stringResource(R.string.home_recent_list_super_img_desc),
                                title = stringResource(R.string.home_recent_list_super_title),
                                subtitle = stringResource(R.string.home_recent_list_super_subtitle)
                            )
                        )
                        ListCard(
                            cardInfo = CardInfo(
                                imageID = R.drawable.list_bbq,
                                imageDescription = stringResource(R.string.home_recent_list_bbq_img_desc),
                                title = stringResource(R.string.home_recent_list_bbq_title),
                                subtitle = stringResource(R.string.home_recent_list_bbq_subtitle)
                            )
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun PreviewHome() {
    TeamMaravillaAppTheme {
        Home(
            onNavigateCreateList = {}
        )
    }
}