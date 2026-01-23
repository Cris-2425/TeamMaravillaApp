package com.example.teammaravillaapp.component

import android.util.Log
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.teammaravillaapp.R
import com.example.teammaravillaapp.model.OptionButton
import com.example.teammaravillaapp.ui.theme.TeamMaravillaAppTheme
import com.example.teammaravillaapp.util.TAG_GLOBAL

@Composable
fun BottomBar(
    selectedButton: OptionButton,
    homeButton: () -> Unit = {},
    profileButton: () -> Unit = {},
    cameraButton: () -> Unit = {},
    recipesButton: () -> Unit = {},
    exitButton: () -> Unit = {}
) {
    NavigationBar {
        NavigationBarItem(
            selected = selectedButton == OptionButton.HOME,
            onClick = {
                Log.d(TAG_GLOBAL, "Ir al inicio")
                homeButton()
            },
            icon = {
                Icon(
                    imageVector = Icons.Default.Home,
                    contentDescription = stringResource(R.string.bottom_bar_home_cd)
                )
            }
        )

        NavigationBarItem(
            selected = selectedButton == OptionButton.PROFILE,
            onClick = {
                Log.d(TAG_GLOBAL, "Ir al perfil")
                profileButton()
            },
            icon = {
                Icon(
                    imageVector = Icons.Default.AccountCircle,
                    contentDescription = stringResource(R.string.bottom_bar_profile_cd)
                )
            }
        )

        NavigationBarItem(
            selected = selectedButton == OptionButton.CAMERA,
            onClick = {
                Log.d(TAG_GLOBAL, "Ir a la cámara")
                cameraButton()
            },
            icon = {
                Icon(
                    imageVector = Icons.Default.PhotoCamera,
                    contentDescription = stringResource(R.string.bottom_bar_camera_cd)
                )
            }
        )

        NavigationBarItem(
            selected = selectedButton == OptionButton.RECIPES,
            onClick = {
                Log.d(TAG_GLOBAL, "Ir a recetas")
                recipesButton()
            },
            icon = {
                Icon(
                    imageVector = Icons.Default.Create,
                    contentDescription = stringResource(R.string.bottom_bar_recipes_cd)
                )
            }
        )

        NavigationBarItem(
            selected = selectedButton == OptionButton.EXIT,
            onClick = {
                Log.d(TAG_GLOBAL, "Salir de la aplicación")
                exitButton()
            },
            icon = {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = stringResource(R.string.bottom_bar_exit_cd)
                )
            }
        )
    }
}

@Preview
@Composable
fun PreviewBottomBarHome() {
    TeamMaravillaAppTheme { BottomBar(selectedButton = OptionButton.HOME) }
}