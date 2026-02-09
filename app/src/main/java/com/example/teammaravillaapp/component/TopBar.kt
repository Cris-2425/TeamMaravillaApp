package com.example.teammaravillaapp.component

import android.util.Log
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.QueryStats
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.teammaravillaapp.R
import com.example.teammaravillaapp.ui.theme.TeamMaravillaAppTheme
import com.example.teammaravillaapp.util.TAG_GLOBAL

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
    title: String,
    onMenuClick: () -> Unit = {},
    onSearchClick: () -> Unit = {},
    onOpenSettings: () -> Unit = {},
    onOpenHelp: () -> Unit = {},
    onOpenStats: () -> Unit = {}
) {
    var moreExpanded by remember { mutableStateOf(false) }

    CenterAlignedTopAppBar(
        title = { Title(texto = title) },
        navigationIcon = {
            IconButton(
                onClick = {
                    Log.d(TAG_GLOBAL, "TopBar → Menú")
                    onMenuClick()
                }
            ) {
                Icon(
                    imageVector = Icons.Default.Menu,
                    contentDescription = stringResource(R.string.topbar_menu_cd)
                )
            }
        },
        actions = {
            IconButton(
                onClick = {
                    Log.d(TAG_GLOBAL, "TopBar → Buscar")
                    onSearchClick()
                }
            ) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = stringResource(R.string.topbar_search_cd)
                )
            }

            // More
            IconButton(
                onClick = {
                    Log.d(TAG_GLOBAL, "TopBar → Más opciones")
                    moreExpanded = true
                }
            ) {
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
                    leadingIcon = {
                        Icon(Icons.Default.Settings, contentDescription = null)
                    },
                    onClick = {
                        moreExpanded = false
                        onOpenSettings()
                    }
                )

                DropdownMenuItem(
                    text = { Text(stringResource(R.string.topbar_menu_stats)) },
                    leadingIcon = {
                        Icon(Icons.Default.QueryStats, contentDescription = null)
                    },
                    onClick = {
                        moreExpanded = false
                        onOpenStats()
                    }
                )

                DropdownMenuItem(
                    text = { Text(stringResource(R.string.topbar_menu_help)) },
                    leadingIcon = {
                        Icon(Icons.Default.HelpOutline, contentDescription = null)
                    },
                    onClick = {
                        moreExpanded = false
                        onOpenHelp()
                    }
                )
            }
        }
    )
}

@Preview
@Composable
fun PreviewTopBar() {
    TeamMaravillaAppTheme {
        TopBar(
            title = "Team Maravilla",
            onMenuClick = {},
            onSearchClick = {},
            onOpenSettings = {},
            onOpenHelp = {},
            onOpenStats = {}
        )
    }
}