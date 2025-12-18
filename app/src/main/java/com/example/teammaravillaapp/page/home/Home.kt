package com.example.teammaravillaapp.page.home

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.teammaravillaapp.R
import com.example.teammaravillaapp.component.*
import com.example.teammaravillaapp.model.CardInfo
import com.example.teammaravillaapp.model.OptionButton
import com.example.teammaravillaapp.model.QuickActionData
import com.example.teammaravillaapp.model.SearchFieldData
import com.example.teammaravillaapp.ui.theme.TeamMaravillaAppTheme
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
    homeViewModel: HomeViewModel = viewModel()
) {
    val uiState by homeViewModel.uiState.collectAsState()

    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()

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
            }
        ) { innerPadding ->

            Box(
                Modifier
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
                            value = uiState.search,
                            placeholder = stringResource(R.string.home_search_placeholder)
                        ),
                        onValueChange = homeViewModel::onSearchChange
                    )

                    Spacer(Modifier.height(24.dp))

                    Text(
                        text = stringResource(R.string.home_recent_lists_title),
                        style = MaterialTheme.typography.titleMedium
                    )

                    Spacer(Modifier.height(8.dp))

                    if (uiState.recentLists.isEmpty()) {
                        Text(
                            text = stringResource(R.string.home_recent_lists_empty),
                            style = MaterialTheme.typography.bodyMedium
                        )
                    } else {
                        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                            uiState.recentLists.forEach { (id, list) ->
                                ListCard(
                                    cardInfo = CardInfo(
                                        imageID = R.drawable.list_supermarket,
                                        imageDescription = list.name,
                                        title = list.name,
                                        subtitle = "${list.products.size} productos"
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

@Preview
@Composable
fun PreviewHome() {
    TeamMaravillaAppTheme {
        Home()
    }
}