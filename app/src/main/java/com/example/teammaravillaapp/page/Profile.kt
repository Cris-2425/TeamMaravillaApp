package com.example.teammaravillaapp.page

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.teammaravillaapp.component.BackButton
import com.example.teammaravillaapp.component.GeneralBackground
import com.example.teammaravillaapp.component.OptionsGrid
import com.example.teammaravillaapp.component.ProfileImage
import com.example.teammaravillaapp.ui.theme.TeamMaravillaAppTheme
import com.example.teammaravillaapp.util.TAG_GLOBAL

/**
 * # Pantalla: **Perfil**
 *
 * Muestra el perfil del usuario: imagen, nombre y un grid de opciones rápidas.
 * Por ahora, es estática (placeholder) y los clicks generan logs.
 */
@Composable
fun Profile() {
    Box(Modifier.fillMaxSize()) {

        GeneralBackground()

        ProfileContent()

        Box(Modifier.align(Alignment.BottomStart)) {
            BackButton(onClick = {
                Log.e(TAG_GLOBAL, "Profile → BackButton")
            }
            )
        }
    }
}

/**
 * Contenido interno del perfil:
 * - Imagen (o placeholder) clickable.
 * - Nombre del usuario.
 * - Cuadrícula de opciones (placeholders).
 */
@Composable
fun ProfileContent() {
    Column(
        Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.height(44.dp))

        ProfileImage(
            imageRes = null,
            modifier = Modifier.clickable {
                Log.e(TAG_GLOBAL, "Click en la imagen de perfil.")
            }
        )

        Spacer(Modifier.height(30.dp))

        Text(
            "Nombre de usuario",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onBackground
        )

        Spacer(Modifier.height(64.dp))

        OptionsGrid(
            options = listOf(
                "Opción 1", "Opción 2", "Opción 3",
                "Opción 4", "Opción 5", "Opción 6"
            )
        )
    }
}

@Preview
@Composable
fun PreviewProfile() {
    TeamMaravillaAppTheme {
        Profile()
    }
}