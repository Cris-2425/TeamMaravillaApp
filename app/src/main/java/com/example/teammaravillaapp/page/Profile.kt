package com.example.teammaravillaapp.page

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.teammaravillaapp.R
import com.example.teammaravillaapp.component.BackButton
import com.example.teammaravillaapp.component.GeneralBackground
import com.example.teammaravillaapp.component.OptionsGrid
import com.example.teammaravillaapp.component.ProfileImage
import com.example.teammaravillaapp.component.Title
import com.example.teammaravillaapp.model.ProfileOption
import com.example.teammaravillaapp.ui.theme.TeamMaravillaAppTheme
import com.example.teammaravillaapp.util.TAG_GLOBAL

/**
 * Pantalla de perfil del usuario.
 *
 * Muestra:
 * - Imagen de perfil (placeholder)
 * - Nombre de usuario
 * - Lista de opciones representadas en burbujas
 *
 * @param onBack Acción al pulsar el botón de volver.
 * @param onNavigate Callback cuando se selecciona una opción.
 */
@Composable
fun Profile(
    onBack: () -> Unit,
    onNavigate: (ProfileOption) -> Unit = {}
) {
    Box(Modifier.fillMaxSize()) {

        GeneralBackground()

        ProfileContent(
            onOptionClick = { option ->
                Log.d(TAG_GLOBAL, "Profile → Click en opción: $option")
                onNavigate(option)
            }
        )

        Box(Modifier.align(Alignment.BottomStart)) {
            BackButton(onClick = {
                Log.d(TAG_GLOBAL, "Profile → BackButton")
                onBack()
            })
        }
    }
}

/**
 * Contenido visible del perfil:
 * - Imagen
 * - Nombre
 * - Grid de opciones
 */
@Composable
fun ProfileContent(
    onOptionClick: (ProfileOption) -> Unit
) {
    val options = ProfileOption.values()

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
                Log.d(TAG_GLOBAL, "Profile → Click en imagen")
            }
        )

        Spacer(Modifier.height(30.dp))

        Text(
            text = stringResource(R.string.profile_username_placeholder),
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onBackground
        )

        Spacer(Modifier.height(64.dp))

        // Pasamos a OptionsGrid solo los textos
        OptionsGrid(
            options = options.map { stringResource(it.labelRes) },
            onOptionClick = { index ->
                onOptionClick(options[index])
            }
        )
    }
}

@Preview
@Composable
fun PreviewProfile() {
    TeamMaravillaAppTheme {
        Profile(
            onBack = {},
            onNavigate = {}
        )
    }
}