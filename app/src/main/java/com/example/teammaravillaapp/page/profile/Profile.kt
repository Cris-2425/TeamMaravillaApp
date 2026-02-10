package com.example.teammaravillaapp.page.profile

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.teammaravillaapp.model.ProfileOption
import com.example.teammaravillaapp.ui.events.UiEvent
import com.example.teammaravillaapp.ui.theme.TeamMaravillaAppTheme

/**
 * Pantalla contenedora de Perfil.
 *
 * Responsabilidades:
 * - Recolectar estado del [ProfileViewModel] (URI de foto de perfil).
 * - Escuchar eventos one-shot del ViewModel y reenviarlos a [onUiEvent].
 * - Gestionar integraciones Android (picker de imagen + recorte) mediante helpers.
 * - Delegar el render a [ProfileContent] (presentación pura).
 *
 * @param onBack Navegación hacia atrás. Restricciones: no nulo.
 * @param onNavigate Navegación a una opción del perfil. Restricciones: no nulo.
 * @param onUiEvent Consumidor de eventos de UI (snackbars). Restricciones: no nulo.
 * @param username Nombre visible. Si es null, se mostrará placeholder.
 * @param isLoggedIn Indica si el usuario tiene sesión iniciada (habilita “Cerrar sesión”).
 * @param vm ViewModel inyectado por Hilt. Se permite override para tests.
 *
 * @see ProfileContent Presentación pura.
 * @see rememberProfileImagePickers Helper para pick/crop.
 *
 * Ejemplo de uso:
 * {@code
 * Profile(
 *   onBack = navController::popBackStack,
 *   onNavigate = { opt -> navController.navigate(opt.route) },
 *   onUiEvent = { event -> handleUiEvent(event) }
 * )
 * }
 */
@Composable
fun Profile(
    onBack: () -> Unit,
    onNavigate: (ProfileOption) -> Unit,
    onUiEvent: (UiEvent) -> Unit,
    username: String? = null,
    isLoggedIn: Boolean = false,
    vm: ProfileViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val photoUriString by vm.photoUri.collectAsStateWithLifecycle()

    LaunchedEffect(vm) {
        vm.events.collect { onUiEvent(it) }
    }

    var showMenu by remember { mutableStateOf(false) }

    val pickers = rememberProfileImagePickers(
        context = context,
        onCropped = { uriString -> vm.savePhoto(uriString) },
        onCropError = vm::onCropError
    )

    ProfileContent(
        username = username,
        isLoggedIn = isLoggedIn,
        photoUriString = photoUriString,
        showPhotoMenu = showMenu,
        onShowPhotoMenuChange = { showMenu = it },
        onPickNewPhoto = {
            showMenu = false
            pickers.pickImage()
        },
        onRemovePhoto = {
            showMenu = false
            vm.clearPhoto()
        },
        onLogout = vm::logout,
        onNavigate = onNavigate,
        onBack = onBack
    )
}

@Preview(showBackground = true)
@Composable
private fun PreviewProfile_LoggedOut_NoPhoto() {
    TeamMaravillaAppTheme {
        ProfileContent(
            username = null,
            isLoggedIn = false,
            photoUriString = null,
            showPhotoMenu = false,
            onShowPhotoMenuChange = {},
            onPickNewPhoto = {},
            onRemovePhoto = {},
            onLogout = {},
            onNavigate = {},
            onBack = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewProfile_LoggedIn_WithPhotoMenuOpen() {
    TeamMaravillaAppTheme {
        ProfileContent(
            username = "Cristian",
            isLoggedIn = true,
            photoUriString = "content://fake/profile.jpg",
            showPhotoMenu = true,
            onShowPhotoMenuChange = {},
            onPickNewPhoto = {},
            onRemovePhoto = {},
            onLogout = {},
            onNavigate = {},
            onBack = {}
        )
    }
}