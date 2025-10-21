package com.example.teammaravillaapp

import android.util.Log
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.runtime.Composable

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
                Log.e("Barra inferior", "Ir a inicio.")
            },
            icon =
        )
    }
    }
}