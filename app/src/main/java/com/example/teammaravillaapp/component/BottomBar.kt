package com.example.teammaravillaapp.component

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.example.teammaravillaapp.R
import com.example.teammaravillaapp.model.OptionButton

@Composable
fun BottomBar(
    selectedButton: OptionButton,
    onHome: () -> Unit,
    onRecipes: () -> Unit,
    onHistory: () -> Unit,
    onProfile: () -> Unit
) {
    NavigationBar {
        NavigationBarItem(
            selected = selectedButton == OptionButton.HOME,
            onClick = onHome,
            icon = {
                Icon(
                    imageVector = Icons.Default.Home,
                    contentDescription = stringResource(R.string.bottom_bar_home_cd)
                )
            },
            label = { androidx.compose.material3.Text(stringResource(R.string.bottom_bar_home_label)) }
        )

        NavigationBarItem(
            selected = selectedButton == OptionButton.RECIPES,
            onClick = onRecipes,
            icon = {
                Icon(
                    imageVector = Icons.Default.Restaurant,
                    contentDescription = stringResource(R.string.bottom_bar_recipes_cd)
                )
            },
            label = { androidx.compose.material3.Text(stringResource(R.string.bottom_bar_recipes_label)) }
        )

        NavigationBarItem(
            selected = selectedButton == OptionButton.HISTORY,
            onClick = onHistory,
            icon = {
                Icon(
                    imageVector = Icons.Default.History,
                    contentDescription = stringResource(R.string.bottom_bar_history_cd)
                )
            },
            label = { androidx.compose.material3.Text(stringResource(R.string.bottom_bar_history_label)) }
        )

        NavigationBarItem(
            selected = selectedButton == OptionButton.PROFILE,
            onClick = onProfile,
            icon = {
                Icon(
                    imageVector = Icons.Default.AccountCircle,
                    contentDescription = stringResource(R.string.bottom_bar_profile_cd)
                )
            },
            label = { androidx.compose.material3.Text(stringResource(R.string.bottom_bar_profile_label)) }
        )
    }
}