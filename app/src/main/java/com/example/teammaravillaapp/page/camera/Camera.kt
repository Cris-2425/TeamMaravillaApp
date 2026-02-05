package com.example.teammaravillaapp.page.camera

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.example.teammaravillaapp.R
import com.example.teammaravillaapp.component.GeneralBackground
import com.example.teammaravillaapp.ui.events.UiEvent

@Composable
fun CameraScreen(
    listId: String?,
    onBack: () -> Unit,
    onUiEvent: (UiEvent) -> Unit,
    vm: CameraViewModel = hiltViewModel()
) {
    val uiState by vm.uiState.collectAsState()

    LaunchedEffect(vm) {
        vm.events.collect { onUiEvent(it) }
    }

    val pickLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        vm.onPicked(uri)
    }

    GeneralBackground(overlayAlpha = 0.18f) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Spacer(Modifier.height(12.dp))

            Text(
                text = if (listId.isNullOrBlank())
                    stringResource(R.string.camera_title_generic)
                else
                    stringResource(R.string.camera_title_receipt),
                style = MaterialTheme.typography.titleLarge
            )

            Text(
                text = if (listId.isNullOrBlank())
                    stringResource(R.string.camera_subtitle_generic)
                else
                    stringResource(R.string.camera_subtitle_receipt),
                style = MaterialTheme.typography.bodyMedium
            )

            Surface(
                shape = MaterialTheme.shapes.medium,
                color = MaterialTheme.colorScheme.surfaceVariant,
                contentColor = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(12.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    if (uiState.pickedUri == null) {
                        Text(
                            text = stringResource(R.string.camera_no_image),
                            style = MaterialTheme.typography.bodyMedium
                        )
                    } else {
                        AsyncImage(
                            model = uiState.pickedUri,
                            contentDescription = stringResource(R.string.camera_image_cd),
                            modifier = Modifier
                                .fillMaxWidth()
                                .heightIn(min = 180.dp)
                        )
                    }
                }
            }

            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Button(
                    onClick = { pickLauncher.launch("image/*") },
                    modifier = Modifier.fillMaxWidth(),
                    contentPadding = PaddingValues(vertical = 14.dp),
                    enabled = !uiState.isSaving
                ) { Text(stringResource(R.string.camera_pick_image)) }

                Button(
                    onClick = { vm.onSave(listId) },
                    enabled = !uiState.isSaving && !listId.isNullOrBlank() && uiState.pickedUri != null,
                    modifier = Modifier.fillMaxWidth(),
                    contentPadding = PaddingValues(vertical = 14.dp)
                ) {
                    Text(
                        if (uiState.isSaving) stringResource(R.string.common_save)
                        else stringResource(R.string.camera_save_receipt)
                    )
                }

                OutlinedButton(
                    onClick = { vm.onClear() },
                    modifier = Modifier.fillMaxWidth(),
                    contentPadding = PaddingValues(vertical = 14.dp),
                    enabled = !uiState.isSaving
                ) { Text(stringResource(R.string.camera_clear_selection)) }
            }
        }

        //Box(Modifier.align(Alignment.BottomStart)) {
        //    BackButton(onClick = onBack)
        //}
    }
}