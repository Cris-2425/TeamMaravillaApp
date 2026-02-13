package com.example.teammaravillaapp.ui.app

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.teammaravillaapp.R
import com.example.teammaravillaapp.component.BottomBar
import com.example.teammaravillaapp.component.DrawerContent
import com.example.teammaravillaapp.component.TopBar
import com.example.teammaravillaapp.model.OptionButton
import com.example.teammaravillaapp.navigation.NavRoute
import com.example.teammaravillaapp.navigation.rememberNavActions
import com.example.teammaravillaapp.ui.events.UiEvent
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch

/**
 * Scaffold principal de la aplicación.
 *
 * Responsabilidades:
 * - Proveer estructura común: TopBar, BottomBar, Drawer, FAB y Snackbars globales.
 * - Delegar el contenido real a [content] (normalmente un NavHost).
 * - Centralizar “plataforma/UI”: permisos de cámara, share intent, snackbars.
 *
 * @param navController Controlador de navegación.
 * @param modifier Modifier para el Scaffold.
 * @param content Contenido de pantalla. Recibe:
 *  - `innerModifier`: padding del Scaffold
 *  - `onUiEvent`: callback para emitir [UiEvent] globales (snackbars, etc.)
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScaffold(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    content: @Composable (innerModifier: Modifier, onUiEvent: (UiEvent) -> Unit) -> Unit
) {
    val actions = rememberNavActions(navController)

    val snackbarHostState = remember { SnackbarHostState() }
    val uiEvents = remember { MutableSharedFlow<UiEvent>(extraBufferCapacity = 64) }
    val scope = rememberCoroutineScope()
    val ctx = LocalContext.current

    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route

    // Pantallas sin barra arriba y abajo
    val authRoutes = remember {
        setOf(
            NavRoute.Splash.route,
            NavRoute.Login.route,
            NavRoute.Register.route
        )
    }

    // Pantallitas con barra abajo
    val bottomTabs = remember {
        setOf(
            NavRoute.Home.route,
            NavRoute.Recipes.route,
            NavRoute.RecipesDetail.route,
            NavRoute.History.route,
            NavRoute.Profile.route,
            NavRoute.ListDetail.route,
            NavRoute.Settings.route,
            NavRoute.Stats.route,
            NavRoute.Help.route
        )
    }

    val showBars = currentRoute !in authRoutes
    val showBottomBar = showBars && currentRoute in bottomTabs
    val showTopBar = showBars && currentRoute in bottomTabs

    // No poner el FAB con la cámara activa
    val isCameraRoute = currentRoute?.startsWith("camera") == true
    val showFab = showBottomBar && !isCameraRoute

    val selectedTab = when (currentRoute) {
        NavRoute.Home.route -> OptionButton.HOME
        NavRoute.Recipes.route -> OptionButton.RECIPES
        NavRoute.History.route -> OptionButton.HISTORY
        NavRoute.Profile.route -> OptionButton.PROFILE
        else -> OptionButton.HOME
    }

    // Título de las pantallas
    val titleRes = when (currentRoute) {
        NavRoute.Home.route -> R.string.home_title
        NavRoute.Recipes.route -> R.string.nav_title_recipes
        NavRoute.RecipesDetail.route -> R.string.recipes_preparation
        NavRoute.History.route -> R.string.nav_title_history
        NavRoute.Profile.route -> R.string.nav_title_profile
        NavRoute.ListDetail.route -> R.string.list_detail_details
        NavRoute.Settings.route -> R.string.settings_title
        NavRoute.Stats.route -> R.string.stats_title
        NavRoute.Help.route -> R.string.help_title
        else -> R.string.app_title
    }

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val enableDrawer = showBars

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted) actions.toCamera()
        else uiEvents.tryEmit(UiEvent.ShowSnackbar(R.string.camera_permission_denied))
    }

    fun openCameraWithPermission() {
        val granted = ContextCompat.checkSelfPermission(ctx, Manifest.permission.CAMERA) ==
                PackageManager.PERMISSION_GRANTED

        if (granted) actions.toCamera()
        else permissionLauncher.launch(Manifest.permission.CAMERA)
    }

    LaunchedEffect(Unit) {
        uiEvents.collect { event ->
            when (event) {
                is UiEvent.ShowSnackbar -> {
                    val msg = ctx.getString(event.messageResId, *event.formatArgs)
                    snackbarHostState.showSnackbar(message = msg)
                }

                is UiEvent.ShowSnackbarAction -> {
                    val msg = ctx.getString(event.messageResId, *event.formatArgs)
                    val action = ctx.getString(event.actionResId)

                    val res = snackbarHostState.showSnackbar(
                        message = msg,
                        actionLabel = action,
                        withDismissAction = event.withDismissAction
                    )

                    if (res == SnackbarResult.ActionPerformed) event.onAction()
                    else event.onDismiss()
                }
            }
        }
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        gesturesEnabled = enableDrawer,
        drawerContent = {
            if (enableDrawer) {
                ModalDrawerSheet {
                    DrawerContent(
                        onNotifications = {
                            scope.launch { drawerState.close() }
                            uiEvents.tryEmit(UiEvent.ShowSnackbar(R.string.drawer_not_implemented))
                        },
                        onShare = {
                            scope.launch { drawerState.close() }

                            val intent = Intent(Intent.ACTION_SEND).apply {
                                type = "text/plain"
                                putExtra(Intent.EXTRA_TEXT, ctx.getString(R.string.drawer_share_text))
                            }
                            ctx.startActivity(
                                Intent.createChooser(
                                    intent,
                                    ctx.getString(R.string.drawer_share_title)
                                )
                            )
                        },
                        onOptions = {
                            scope.launch { drawerState.close() }
                            actions.toSettings()
                        },
                        onExit = {
                            scope.launch { drawerState.close() }
                            uiEvents.tryEmit(UiEvent.ShowSnackbar(R.string.drawer_exit_hint))
                        }
                    )
                }
            } else {
                ModalDrawerSheet { }
            }
        }
    ) {
        Scaffold(
            modifier = modifier,
            topBar = {
                if (showTopBar) {
                    TopBar(
                        title = ctx.getString(titleRes),
                        onMenuClick = { scope.launch { drawerState.open() } },
                        onSearchClick = {
                            actions.toHomeSingleTop()

                            val homeEntry = runCatching {
                                navController.getBackStackEntry(NavRoute.Home.route)
                            }.getOrNull()

                            (homeEntry ?: navController.currentBackStackEntry)
                                ?.savedStateHandle
                                ?.set("focusSearch", true)
                        },
                        onOpenSettings = { actions.toSettings() },
                        onOpenHelp = { actions.toHelp() },
                        onOpenStats = { actions.toStats() }
                    )
                }
            },
            bottomBar = {
                if (showBottomBar) {
                    BottomBar(
                        selectedButton = selectedTab,
                        onHome = { actions.toHomeSingleTop() },
                        onRecipes = { actions.toRecipesSingleTop() },
                        onHistory = { actions.toHistorySingleTop() },
                        onProfile = { actions.toProfileSingleTop() }
                    )
                }
            },
            floatingActionButton = {
                if (showFab) {
                    FloatingActionButton(onClick = ::openCameraWithPermission) {
                        Icon(
                            imageVector = Icons.Default.PhotoCamera,
                            contentDescription = ctx.getString(R.string.fab_open_camera_cd)
                        )
                    }
                }
            },
            snackbarHost = { SnackbarHost(snackbarHostState) }
        ) { padding ->
            content(Modifier.padding(padding)) { uiEvents.tryEmit(it) }
        }
    }
}