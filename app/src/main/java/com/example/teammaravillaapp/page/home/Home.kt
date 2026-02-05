package com.example.teammaravillaapp.page.home

import RenameListDialog
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.teammaravillaapp.R
import com.example.teammaravillaapp.component.BottomBar
import com.example.teammaravillaapp.component.DrawerContent
import com.example.teammaravillaapp.component.GeneralBackground
import com.example.teammaravillaapp.component.ListCard
import com.example.teammaravillaapp.component.QuickActionsRow
import com.example.teammaravillaapp.component.SearchField
import com.example.teammaravillaapp.component.SectionCard
import com.example.teammaravillaapp.component.SwipeRowActions
import com.example.teammaravillaapp.component.TopBar
import com.example.teammaravillaapp.model.OptionButton
import com.example.teammaravillaapp.model.QuickActionData
import com.example.teammaravillaapp.model.SearchFieldData
import com.example.teammaravillaapp.ui.events.UiEvent
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
    onUiEvent: (UiEvent) -> Unit = {},
    vm: HomeViewModel = hiltViewModel()
) {
    val uiState by vm.uiState.collectAsState()
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    var renameTarget by remember { mutableStateOf<Pair<String, String>?>(null) }
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(vm) {
        vm.events.collect { onUiEvent(it) }
    }

    val labelNewList = stringResource(R.string.home_quick_new_list)
    val labelFavorites = stringResource(R.string.home_quick_favorites)
    val labelHistory = stringResource(R.string.home_quick_history)
    val msgDeleted = stringResource(R.string.home_snackbar_list_deleted)
    val labelUndo = stringResource(R.string.common_undo)

    val quickActions = listOf(
        QuickActionData(Icons.Default.ShoppingCart, labelNewList),
        QuickActionData(Icons.Default.Favorite, labelFavorites),
        QuickActionData(Icons.Default.Create, labelHistory)
    )

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
                            onValueChange = vm::onSearchChange
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

                    // --- Lists ---
                    SectionCard {
                        Text(
                            text = stringResource(R.string.home_recent_lists_title),
                            style = MaterialTheme.typography.titleMedium
                        )

                        Spacer(Modifier.height(10.dp))

                        if (uiState.rows.isEmpty()) {
                            Text(
                                text = stringResource(R.string.home_recent_lists_empty),
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        } else {
                            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                                uiState.rows.forEach { row ->
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

                                    SwipeRowActions(
                                        id = id,
                                        onEdit = { renameTarget = id to list.name },
                                        onDelete = {
                                            scope.launch {
                                                vm.requestDelete(id)

                                                val res = snackbarHostState.showSnackbar(
                                                    message = msgDeleted,
                                                    actionLabel = labelUndo,
                                                    withDismissAction = true
                                                )

                                                if (res == SnackbarResult.ActionPerformed) {
                                                    vm.undoDelete(id)
                                                } else {
                                                    vm.commitDelete(id)
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
                                                onClick = {
                                                    vm.onOpenList(id)
                                                    onOpenList(id)
                                                }
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

                renameTarget?.let { (listId, currentName) ->
                    RenameListDialog(
                        currentName = currentName,
                        onDismiss = { renameTarget = null },
                        onConfirm = { newName ->
                            renameTarget = null
                            vm.renameList(listId, newName)
                        }
                    )
                }
            }
        }
    }
}