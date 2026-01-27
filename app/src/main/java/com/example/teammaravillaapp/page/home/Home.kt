package com.example.teammaravillaapp.page.home

import RenameListDialog
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.teammaravillaapp.R
import com.example.teammaravillaapp.component.*
import com.example.teammaravillaapp.data.prefs.RecentListsPrefs
import com.example.teammaravillaapp.model.OptionButton
import com.example.teammaravillaapp.model.QuickActionData
import com.example.teammaravillaapp.model.SearchFieldData
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Home(
    onNavigateCreateList: () -> Unit = {},
    onNavigateHome: () -> Unit = {},
    onNavigateProfile: () -> Unit = {},
    onNavigateCamera: () -> Unit = {},
    onNavigateRecipes: () -> Unit = {},
    onExitApp: () -> Unit = {},
    onOpenList: (String) -> Unit = {},
    onNavigateHistory: () -> Unit = {},
    homeViewModel: HomeViewModel = hiltViewModel()
) {
    val uiState by homeViewModel.uiState.collectAsState()

    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    // ✅ Rename (una sola vez)
    var renameTarget by remember { mutableStateOf<Pair<String, String>?>(null) }

    // ✅ Snackbar para "Deshacer" y también "Próximamente"
    val snackbarHostState = remember { SnackbarHostState() }

    val labelNewList = stringResource(R.string.home_quick_new_list)
    val labelFavorites = stringResource(R.string.home_quick_favorites)
    val labelHistory = stringResource(R.string.home_quick_history)

    val quickActions = listOf(
        QuickActionData(Icons.Default.ShoppingCart, labelNewList),
        QuickActionData(Icons.Default.Favorite, labelFavorites),
        QuickActionData(Icons.Default.Create, labelHistory)
    )
    val ctx = LocalContext.current

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            DrawerContent(
                onNotifications = { scope.launch { drawerState.close() } },
                onShare = { scope.launch { drawerState.close() } },
                onOptions = { scope.launch { drawerState.close() } },
                onExit = {
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
                    onMenuClick = { scope.launch { drawerState.open() } },
                    onSearchClick = {},
                    onMoreClick = {}
                )
            },
            bottomBar = {
                BottomBar(
                    selectedButton = OptionButton.HOME,
                    homeButton = onNavigateHome,
                    profileButton = onNavigateProfile,
                    cameraButton = onNavigateCamera,
                    recipesButton = onNavigateRecipes,
                    exitButton = onExitApp
                )
            },
            floatingActionButton = {
                FloatingActionButton(
                    onClick = onNavigateCreateList,
                    containerColor = MaterialTheme.colorScheme.secondary
                ) {
                    Icon(Icons.Default.Add, contentDescription = null)
                }
            },
            snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
        ) { innerPadding ->

            GeneralBackground(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
            ) {
                Column(
                    Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp, vertical = 14.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {

                    Text(
                        text = stringResource(R.string.home_title),
                        style = MaterialTheme.typography.headlineMedium
                    )

                    // --- Search + quick actions ---
                    SectionCard {
                        SearchField(
                            searchData = SearchFieldData(
                                value = uiState.search,
                                placeholder = stringResource(R.string.home_search_placeholder)
                            ),
                            onValueChange = homeViewModel::onSearchChange
                        )

                        Spacer(Modifier.height(12.dp))

                        QuickActionsRow(
                            actions = quickActions,
                            onClick = { action ->
                                when (action.label) {
                                    labelNewList -> onNavigateCreateList()

                                    labelFavorites -> onNavigateRecipes()

                                    labelHistory -> onNavigateHistory()
                                }
                            }
                        )
                    }

                    // --- Recent lists ---
                    SectionCard {
                        Text(
                            text = stringResource(R.string.home_recent_lists_title),
                            style = MaterialTheme.typography.titleMedium
                        )

                        Spacer(Modifier.height(10.dp))

                        if (uiState.recentLists.isEmpty()) {
                            Text(
                                text = stringResource(R.string.home_recent_lists_empty),
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        } else {
                            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                                uiState.recentLists.forEach { row ->
                                    val id = row.id
                                    val list = row.list
                                    val p = row.progress

                                    val subtitle = if (p.totalCount == 0) {
                                        stringResource(R.string.home_list_subtitle_empty)
                                    } else {
                                        stringResource(
                                            R.string.home_list_subtitle_progress,
                                            p.checkedCount,
                                            p.totalCount
                                        )
                                    }

                                    // ✅ Swipe derecha editar, izquierda borrar + deshacer
                                    SwipeRowActions(
                                        id = id,
                                        onEdit = { renameTarget = id to list.name },
                                        onDelete = {
                                            scope.launch {
                                                // (opcional) si quieres que desaparezca del historial ya al arrastrar:
                                                // RecentListsPrefs.remove(ctx, id)

                                                homeViewModel.requestDelete(id)

                                                val res = snackbarHostState.showSnackbar(
                                                    message = "Lista eliminada",
                                                    actionLabel = "Deshacer",
                                                    withDismissAction = true
                                                )

                                                if (res == SnackbarResult.ActionPerformed) {
                                                    homeViewModel.undoDelete(id)
                                                } else {
                                                    // ✅ commit: borramos de Room y limpiamos historial
                                                    homeViewModel.commitDelete(id)
                                                    RecentListsPrefs.remove(ctx, id) // 👈 IMPORTANTE
                                                }
                                            }
                                        }

                                    ) {
                                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                            ListCard(
                                                cardInfo = com.example.teammaravillaapp.model.CardInfo(
                                                    imageID = R.drawable.list_supermarket,
                                                    imageDescription = list.name,
                                                    title = list.name,
                                                    subtitle = subtitle
                                                ),
                                                modifier = Modifier.fillMaxWidth(),
                                                onClick = { onOpenList(id) }
                                            )

                                            if (p.totalCount > 0) {
                                                LinearProgressIndicator(
                                                    progress = { p.checkedCount.toFloat() / p.totalCount.toFloat() },
                                                    modifier = Modifier.fillMaxWidth()
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                // ✅ Dialog SOLO una vez (fuera del forEach)
                renameTarget?.let { (listId, currentName) ->
                    RenameListDialog(
                        currentName = currentName,
                        onDismiss = { renameTarget = null },
                        onConfirm = { newName ->
                            renameTarget = null
                            homeViewModel.renameList(listId, newName)
                        }
                    )
                }
            }
        }
    }
}
