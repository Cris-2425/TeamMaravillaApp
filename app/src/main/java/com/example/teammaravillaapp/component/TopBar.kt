package com.example.teammaravillaapp.component

import android.util.Log
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.teammaravillaapp.ui.theme.TeamMaravillaAppTheme
import com.example.teammaravillaapp.util.TAG_GLOBAL

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
    title: String,
    onMenuClick: () -> Unit = {},
    onSearchClick: () -> Unit = {},
    onMoreClick: () -> Unit = {}
) {
    CenterAlignedTopAppBar(
        title = {
            Title(title)
                },
        navigationIcon = {
            IconButton(
                onClick = {
                    Log.e(TAG_GLOBAL, "TopBar → clic en 'Menú'")
                    onMenuClick()
                }
            ) {
                Icon(Icons.Default.Menu, contentDescription = "Menu")
            }
        },
        actions = {
            IconButton(
                onClick = {
                    Log.e(TAG_GLOBAL, "TopBar → clic en 'Buscar'")
                    onSearchClick()
                }
            ) {
                Icon(Icons.Default.Search, contentDescription = "Search")
            }
            IconButton(
                onClick = {
                    Log.e(TAG_GLOBAL, "TopBar → clic en 'Más opciones'")
                    onMoreClick()
                }
            ) {
                Icon(Icons.Default.MoreVert, contentDescription = "More")
            }
        }
    )
}

@Preview
@Composable
fun PreviewTopBarInicio() {
    TeamMaravillaAppTheme {
        TopBar("Team Maravilla")
    }
}