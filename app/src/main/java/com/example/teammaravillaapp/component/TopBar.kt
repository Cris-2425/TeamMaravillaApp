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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.teammaravillaapp.R
import com.example.teammaravillaapp.ui.theme.TeamMaravillaAppTheme
import com.example.teammaravillaapp.util.TAG_GLOBAL

/**
 * AppBar superior centrada con **menú**, **buscar** y **más**.
 *
 * @param title texto a mostrar (puede ser “Team Maravilla” para ver logo en Title()).
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
    title: String,
    onMenuClick: () -> Unit = {},
    onSearchClick: () -> Unit = {},
    onMoreClick: () -> Unit = {}
) {
    CenterAlignedTopAppBar(
        title = { Title(texto = title) },
        navigationIcon = {
            IconButton(
                onClick = {
                    Log.e(TAG_GLOBAL, "Click en 'Menú'")
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
                    Log.e(TAG_GLOBAL, "Click en 'Buscar'")
                    onSearchClick()
                }
            ) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = stringResource(R.string.topbar_search_cd)
                )
            }
            IconButton(
                onClick = {
                    Log.e(TAG_GLOBAL, "Click en 'Más opciones'")
                    onMoreClick()
                }
            ) {
                Icon(
                    imageVector = Icons.Default.MoreVert,
                    contentDescription = stringResource(R.string.topbar_more_cd)
                )
            }
        }
    )
}

@Preview
@Composable
fun PreviewTopBar() {
    TeamMaravillaAppTheme {
        TopBar("Team Maravilla")
    }
}