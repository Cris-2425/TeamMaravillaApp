package com.example.teammaravillaapp.component

import android.util.Log
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview

enum class BotonOpciones { INICIO, PERFIL, CAMARA, RECETAS, SALIR }

@Composable
fun BottomBar(
    botonSeleccionado: BotonOpciones,
    botonInicio: () -> Unit = {},
    botonPerfil: () -> Unit = {},
    botonCamara: () -> Unit = {},
    botonRecetas: () -> Unit = {},
    botonSalir: () -> Unit = {}
) {
    NavigationBar {
        NavigationBarItem(
            selected = botonSeleccionado == BotonOpciones.INICIO,
                onClick = {
                    Log.e("Barra inferior", "Ir al inicio")
                    botonInicio()
            },
            icon = { Icon(Icons.Default.Home, contentDescription = "Inicio") }
        )
        NavigationBarItem(
            selected = botonSeleccionado == BotonOpciones.PERFIL,
            onClick = {
                Log.e("Barra inferior", "Ir al perfil")
                botonPerfil
            },
            icon = { Icon(Icons.Default.AccountCircle, contentDescription = "Perfil") }
        )
        NavigationBarItem(
            selected = botonSeleccionado == BotonOpciones.CAMARA,
            onClick = {
                Log.e("Barra inferior", "Ir a la cámara")
                botonCamara()
            },
            icon = { Icon(Icons.Default.AddCircle, contentDescription = "Cámara") }
        )
        NavigationBarItem(
            selected = botonSeleccionado == BotonOpciones.RECETAS,
            onClick = {
                Log.e("Barra inferior", "Ir a recetas")
                botonRecetas()
            },
            icon = { Icon(Icons.Default.Create, contentDescription = "Recetas") }
        )
        NavigationBarItem(
            selected = botonSeleccionado == BotonOpciones.SALIR,
            onClick = {
                Log.e("Barra inferior", "Salir de la aplicación")
                botonSalir()
            },
            icon = { Icon(Icons.Default.Close, contentDescription = "Salir") }
        )
    }
}


@Preview
@Composable
fun PreviewBottomBarInicio() {
    MaterialTheme {
        BottomBar( botonSeleccionado = BotonOpciones.INICIO)
    }
}

@Preview
@Composable
fun PreviewBottomBarPerfil() {
    MaterialTheme {
        BottomBar( botonSeleccionado = BotonOpciones.PERFIL)
    }
}

@Preview
@Composable
fun PreviewBottomBarCamara() {
    MaterialTheme {
        BottomBar( botonSeleccionado = BotonOpciones.CAMARA)
    }
}

@Preview
@Composable
fun PreviewBottomBarRecetas() {
    MaterialTheme {
        BottomBar( botonSeleccionado = BotonOpciones.RECETAS)
    }
}

@Preview
@Composable
fun PreviewBottomBarSalir() {
    MaterialTheme {
        BottomBar( botonSeleccionado = BotonOpciones.SALIR)
    }
}