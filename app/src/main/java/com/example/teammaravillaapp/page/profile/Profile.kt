package com.example.teammaravillaapp.page.profile

import android.app.Activity
import android.content.Intent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.teammaravillaapp.R
import com.example.teammaravillaapp.component.BackButton
import com.example.teammaravillaapp.component.GeneralBackground
import com.example.teammaravillaapp.component.OptionsGrid
import com.example.teammaravillaapp.component.ProfileImage
import com.example.teammaravillaapp.data.prefs.clearProfilePhoto
import com.example.teammaravillaapp.data.prefs.profilePhotoFlow
import com.example.teammaravillaapp.data.prefs.saveProfilePhoto
import com.example.teammaravillaapp.model.ProfileOption
import com.example.teammaravillaapp.ui.events.UiEvent
import com.yalantis.ucrop.UCrop
import kotlinx.coroutines.launch
import java.io.File

@Composable
fun Profile(
    onBack: () -> Unit,
    onNavigate: (ProfileOption) -> Unit,
    onUiEvent: (UiEvent) -> Unit,
    username: String? = null,
    isLoggedIn: Boolean = false,
    vm: ProfileViewModel = hiltViewModel()
) {
    val ctx = LocalContext.current
    val scope = rememberCoroutineScope()

    LaunchedEffect(vm) {
        vm.events.collect { onUiEvent(it) }
    }

    val profileUri by profilePhotoFlow(ctx).collectAsStateWithLifecycle(initialValue = null)

    var showMenu by remember { mutableStateOf(false) }

    val cropLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        val data: Intent? = result.data
        if (result.resultCode == Activity.RESULT_OK && data != null) {
            val resultUri = UCrop.getOutput(data)
            resultUri?.let { uri ->
                scope.launch { saveProfilePhoto(ctx, uri.toString()) }
            }
        } else if (result.resultCode == UCrop.RESULT_ERROR) {
            vm.onCropError()
        }
    }

    val pickImageLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri ->
        if (uri != null) {
            val destUri = android.net.Uri.fromFile(
                File(ctx.cacheDir, "profile_${System.currentTimeMillis()}.jpg")
            )

            val intent = UCrop.of(uri, destUri)
                .withAspectRatio(1f, 1f)
                .withMaxResultSize(700, 700)
                .getIntent(ctx)

            cropLauncher.launch(intent)
        }
    }

    Box(Modifier.fillMaxSize()) {
        GeneralBackground(overlayAlpha = 0.20f) {

            Column(
                Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp, vertical = 14.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(Modifier.height(10.dp))

                Box {
                    ProfileImage(
                        imageRes = null,
                        uriString = profileUri,
                        modifier = Modifier.clickable { showMenu = true }
                    )

                    DropdownMenu(
                        expanded = showMenu,
                        onDismissRequest = { showMenu = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text(stringResource(R.string.profile_change_photo)) },
                            onClick = {
                                showMenu = false
                                pickImageLauncher.launch("image/*")
                            }
                        )
                        DropdownMenuItem(
                            text = { Text(stringResource(R.string.profile_remove_photo)) },
                            enabled = !profileUri.isNullOrBlank(),
                            onClick = {
                                showMenu = false
                                scope.launch { clearProfilePhoto(ctx) }
                            }
                        )
                    }
                }

                Spacer(Modifier.height(14.dp))

                Surface(
                    shape = MaterialTheme.shapes.large,
                    tonalElevation = 2.dp,
                    color = MaterialTheme.colorScheme.surface
                ) {
                    Column(
                        Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = username ?: stringResource(R.string.profile_username_placeholder),
                            style = MaterialTheme.typography.titleMedium
                        )
                        Spacer(Modifier.height(4.dp))
                        Text(
                            text = if (isLoggedIn)
                                stringResource(R.string.profile_status_logged_in)
                            else
                                stringResource(R.string.profile_status_logged_out),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                Spacer(Modifier.height(16.dp))

                val options = ProfileOption.entries.toTypedArray()

                Surface(
                    shape = MaterialTheme.shapes.extraLarge,
                    tonalElevation = 2.dp,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(Modifier.padding(14.dp)) {
                        OptionsGrid(
                            options = options.map { stringResource(it.labelRes) },
                            onOptionClick = { index -> onNavigate(options[index]) }
                        )

                        if (isLoggedIn) {
                            Spacer(Modifier.height(10.dp))
                            OutlinedButton(
                                onClick = { vm.logout() },
                                modifier = Modifier.fillMaxWidth(),
                                colors = ButtonDefaults.outlinedButtonColors(
                                    contentColor = MaterialTheme.colorScheme.error
                                ),
                                border = ButtonDefaults.outlinedButtonBorder.copy(
                                    brush = androidx.compose.ui.graphics.SolidColor(
                                        MaterialTheme.colorScheme.error
                                    )
                                )
                            ) {
                                Text(stringResource(R.string.profile_logout))
                            }
                        }
                    }
                }
            }

            //Box(Modifier.align(Alignment.BottomStart)) {
            //    BackButton(onClick = onBack)
            //}
        }
    }
}