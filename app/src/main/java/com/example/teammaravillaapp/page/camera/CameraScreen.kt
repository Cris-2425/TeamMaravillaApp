package com.example.teammaravillaapp.page.camera

import android.net.Uri
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import coil.compose.AsyncImage
import com.example.teammaravillaapp.R
import com.example.teammaravillaapp.component.BackButton
import com.example.teammaravillaapp.component.GeneralBackground
import kotlinx.coroutines.launch
import com.example.teammaravillaapp.data.prefs.userPrefsDataStore

@Composable
fun CameraScreen(
    listId: String?,
    onBack: () -> Unit
) {
    val ctx = LocalContext.current
    val scope = rememberCoroutineScope()

    var pickedUri by remember { mutableStateOf<Uri?>(null) }
    var message by remember { mutableStateOf<String?>(null) }

    // ✅ Compatible: abre galería y devuelve Uri
    val pickLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        pickedUri = uri
        message = null
    }

    Box(Modifier.fillMaxSize()) {
        GeneralBackground()

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
                    if (pickedUri == null) {
                        Text(
                            text = stringResource(R.string.camera_no_image),
                            style = MaterialTheme.typography.bodyMedium
                        )
                    } else {
                        AsyncImage(
                            model = pickedUri,
                            contentDescription = stringResource(R.string.camera_image_cd),
                            modifier = Modifier
                                .fillMaxWidth()
                                .heightIn(min = 180.dp)
                        )
                    }

                    message?.let {
                        Text(text = it, style = MaterialTheme.typography.bodyMedium)
                    }
                }
            }

            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Button(
                    onClick = { pickLauncher.launch("image/*") },
                    modifier = Modifier.fillMaxWidth(),
                    contentPadding = PaddingValues(vertical = 14.dp)
                ) {
                    Text(stringResource(R.string.camera_pick_image))
                }

                Button(
                    onClick = {
                        val id = listId
                        val uri = pickedUri

                        if (id.isNullOrBlank()) {
                            message = ctx.getString(R.string.camera_error_no_list)
                            return@Button
                        }
                        if (uri == null) {
                            message = ctx.getString(R.string.camera_error_no_image)
                            return@Button
                        }

                        val key = stringPreferencesKey("receipt_uri_$id")
                        scope.launch {
                            ctx.userPrefsDataStore.edit { prefs ->
                                prefs[key] = uri.toString()
                            }
                            message = ctx.getString(R.string.camera_saved_ok)
                        }
                    },
                    enabled = !listId.isNullOrBlank() && pickedUri != null,
                    modifier = Modifier.fillMaxWidth(),
                    contentPadding = PaddingValues(vertical = 14.dp)
                ) {
                    Text(stringResource(R.string.camera_save_receipt))
                }

                OutlinedButton(
                    onClick = { pickedUri = null; message = null },
                    modifier = Modifier.fillMaxWidth(),
                    contentPadding = PaddingValues(vertical = 14.dp)
                ) {
                    Text(stringResource(R.string.camera_clear_selection))
                }
            }
        }

        Box(Modifier.align(Alignment.BottomStart)) {
            BackButton(onClick = onBack)
        }
    }
}