package com.example.teammaravillaapp.component

import android.util.Log
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.teammaravillaapp.model.optionButton
import com.example.teammaravillaapp.ui.theme.TeamMaravillaAppTheme
import com.example.teammaravillaapp.util.TAG_GLOBAL

@Composable
fun BottomBar(
    selectedButton: optionButton,
    homeButton: () -> Unit = {},
    profileButton: () -> Unit = {},
    cameraButton: () -> Unit = {},
    recipesButton: () -> Unit = {},
    exitButton: () -> Unit = {}
) {
    NavigationBar {
        NavigationBarItem(
            selected = selectedButton == optionButton.HOME,
            onClick = {
                Log.e(TAG_GLOBAL, "Ir al inicio")
                homeButton()
            },
            icon = { Icon(Icons.Default.Home, contentDescription = "Inicio") }
        )
        NavigationBarItem(
            selected = selectedButton == optionButton.PROFILE,
            onClick = {
                Log.e(TAG_GLOBAL, "Ir al perfil")
                profileButton()
            },
            icon = { Icon(Icons.Default.AccountCircle, contentDescription = "Perfil") }
        )
        NavigationBarItem(
            selected = selectedButton == optionButton.CAMERA,
            onClick = {
                Log.e(TAG_GLOBAL, "Ir a la cámara")
                cameraButton()
            },
            icon = { Icon(Icons.Default.AddCircle, contentDescription = "Cámara") }
        )
        NavigationBarItem(
            selected = selectedButton == optionButton.RECIPES,
            onClick = {
                Log.e(TAG_GLOBAL, "Ir a recetas")
                recipesButton()
            },
            icon = { Icon(Icons.Default.Create, contentDescription = "Recetas") }
        )
        NavigationBarItem(
            selected = selectedButton == optionButton.EXIT,
            onClick = {
                Log.e(TAG_GLOBAL, "Salir de la aplicación")
                exitButton()
            },
            icon = { Icon(Icons.Default.Close, contentDescription = "Salir") }
        )
    }
}

@Preview
@Composable
fun PreviewBottomBarHome() {
    TeamMaravillaAppTheme {
        BottomBar( selectedButton = optionButton.HOME)
    }
}

@Preview
@Composable
fun PreviewBottomBarPerfil() {
    TeamMaravillaAppTheme {
        BottomBar( selectedButton = optionButton.PROFILE)
    }
}

@Preview
@Composable
fun PreviewBottomBarCamera() {
    TeamMaravillaAppTheme {
        BottomBar( selectedButton = optionButton.CAMERA)
    }
}

@Preview
@Composable
fun PreviewBottomBarRecipes() {
    TeamMaravillaAppTheme {
        BottomBar( selectedButton = optionButton.RECIPES)
    }
}

@Preview
@Composable
fun PreviewBottomBarExit() {
    TeamMaravillaAppTheme {
        BottomBar( selectedButton = optionButton.EXIT)
    }
}