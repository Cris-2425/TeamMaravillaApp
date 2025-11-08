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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.teammaravillaapp.R
import com.example.teammaravillaapp.component.*
import com.example.teammaravillaapp.model.optionButton
import com.example.teammaravillaapp.ui.theme.TeamMaravillaAppTheme
import com.example.teammaravillaapp.util.TAG_GLOBAL
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Home() {
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    var search by remember { mutableStateOf("") }

    val quickActions = listOf(
        QuickActionData(Icons.Default.ShoppingCart, "Nueva lista"),
        QuickActionData(Icons.Default.Favorite,   "Favoritos"),
        QuickActionData(Icons.Default.Create,     "Historial")
    )

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            DrawerContent(
                onNotifications = {
                    Log.e(TAG_GLOBAL, "Home → Drawer: Notificaciones")
                    scope.launch { drawerState.close() }
                },
                onShare = {
                    Log.e(TAG_GLOBAL, "Home → Drawer: Compartir lista")
                    scope.launch { drawerState.close() }
                },
                onOptions = {
                    Log.e(TAG_GLOBAL, "Home → Drawer: Opciones")
                    scope.launch { drawerState.close() }
                },
                onExit = {
                    Log.e(TAG_GLOBAL, "Home → Drawer: Salir")
                    scope.launch { drawerState.close() }
                }
            )
        }
    ) {
        Scaffold(
            topBar = {
                TopBar(
                    title = "Team Maravilla",
                    onMenuClick = {
                        Log.e(TAG_GLOBAL, "Home → TopBar: abrir menú")
                        scope.launch { drawerState.open() }
                    },
                    onSearchClick = {
                        Log.e(TAG_GLOBAL, "Home → TopBar: buscar")
                    },
                    onMoreClick = {
                        Log.e(TAG_GLOBAL, "Home → TopBar: más opciones")
                    }
                )
            },
            bottomBar = {
                BottomBar(selectedButton = optionButton.HOME)
            },
            floatingActionButton = {
                FloatingActionButton(
                    onClick = {
                        Log.e(TAG_GLOBAL, "Home → FAB: Añadir")
                    },
                    containerColor = MaterialTheme.colorScheme.secondary,
                    contentColor = MaterialTheme.colorScheme.onSecondary
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Añadir")
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
                    Title("Inicio")

                    Spacer(Modifier.height(16.dp))

                    SearchField(
                        searchData = SearchFieldData(search, "Buscar producto o lista"),
                        onValueChange = {
                            search = it
                            Log.e(TAG_GLOBAL, "Home → SearchField: '$it'")
                        }
                    )

                    Spacer(Modifier.height(20.dp))

                    Text("Acciones rápidas", style = MaterialTheme.typography.titleMedium)
                    Spacer(Modifier.height(8.dp))

                    Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                        quickActions.forEach { qa ->
                            QuickActionButton(
                                quickActionButtonData = qa,
                                onClick = {
                                    Log.e(TAG_GLOBAL, "Home → QuickAction: ${qa.label}")
                                }
                            )
                        }
                    }

                    Spacer(Modifier.height(24.dp))

                    Text("Listas recientes", style = MaterialTheme.typography.titleMedium)
                    Spacer(Modifier.height(8.dp))

                    Column(
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                    ) {
                        ListCard(
                            cardInfo = CardInfo(
                                imageID = R.drawable.list_supermarket,
                                imageDescription = "Lista supermercado",
                                title = "Compra semanal",
                                subtitle = "Supermercado"
                            )
                        )
                        ListCard(
                            cardInfo = CardInfo(
                                imageID = R.drawable.list_bbq,
                                imageDescription = "Lista bbq",
                                title = "BBQ sábado",
                                subtitle = "Carnicería"
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
        Home()
    }
}