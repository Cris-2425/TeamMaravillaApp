package com.example.teammaravillaapp.page.camera

import android.Manifest
import android.content.ContentValues
import android.net.Uri
import android.provider.MediaStore
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.ImageCapture
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cached
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material.icons.filled.FlashOff
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.example.teammaravillaapp.component.GeneralBackground
import com.example.teammaravillaapp.ui.events.UiEvent
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun CameraScreen(
    listId: String?,
    onBack: () -> Unit,
    onUiEvent: (UiEvent) -> Unit,
    vm: CameraViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val uiState by vm.uiState.collectAsState()

    LaunchedEffect(vm) { vm.events.collect { onUiEvent(it) } }

    val controller = remember { CameraXController(context) }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { granted ->
        vm.onPermissionResult(granted)
    }

    LaunchedEffect(Unit) {
        permissionLauncher.launch(Manifest.permission.CAMERA)
    }

    // Re-bindea cuando cambie lente/permisos
    var previewViewRef by remember { mutableStateOf<PreviewView?>(null) }

    LaunchedEffect(uiState.hasPermission, uiState.lensFacing) {
        if (uiState.hasPermission && uiState.capturedUri == null) {
            previewViewRef?.let { pv ->
                controller.bind(
                    previewView = pv,
                    lifecycleOwner = lifecycleOwner,
                    lensFacing = uiState.lensFacing,
                    flashEnabled = uiState.flashEnabled
                )
            }
        }
    }

    LaunchedEffect(uiState.flashEnabled) {
        controller.updateFlash(uiState.flashEnabled)
    }

    DisposableEffect(Unit) {
        onDispose { controller.unbind() }
    }

    fun createOutputUri(): Uri? {
        // Guarda en MediaStore (Galería)
        val time = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val name = "TM_receipt_$time"

        val values = ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, "$name.jpg")
            put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
            put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/TeamMaravilla")
        }

        return context.contentResolver.insert(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            values
        )
    }

    GeneralBackground(overlayAlpha = 0.18f) {
        Column(
            Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "Cámara (CameraX)",
                style = MaterialTheme.typography.titleLarge
            )

            if (!uiState.hasPermission) {
                Surface(
                    shape = MaterialTheme.shapes.medium,
                    tonalElevation = 1.dp,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Text("No hay permiso de cámara.")
                        Button(onClick = { permissionLauncher.launch(Manifest.permission.CAMERA) }) {
                            Text("Dar permiso")
                        }
                    }
                }
                return@GeneralBackground
            }

            // Si ya capturó: preview de la foto
            if (uiState.capturedUri != null) {
                Surface(
                    shape = MaterialTheme.shapes.large,
                    tonalElevation = 1.dp,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    AsyncImage(
                        model = uiState.capturedUri,
                        contentDescription = "Foto capturada",
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(min = 240.dp)
                    )
                }

                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    OutlinedButton(
                        onClick = vm::retake,
                        modifier = Modifier.weight(1f),
                        enabled = !uiState.isSaving
                    ) { Text("Repetir") }

                    Button(
                        onClick = { vm.saveReceipt(listId) },
                        modifier = Modifier.weight(1f),
                        enabled = !uiState.isSaving && !listId.isNullOrBlank()
                    ) { Text(if (uiState.isSaving) "Guardando..." else "Guardar ticket") }
                }

                return@GeneralBackground
            }

            // Preview CameraX
            Surface(
                shape = MaterialTheme.shapes.large,
                tonalElevation = 1.dp,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(420.dp)
            ) {
                AndroidView(
                    modifier = Modifier.fillMaxSize(),
                    factory = { ctx ->
                        PreviewView(ctx).also { pv ->
                            pv.scaleType = PreviewView.ScaleType.FILL_CENTER
                            previewViewRef = pv

                            controller.bind(
                                previewView = pv,
                                lifecycleOwner = lifecycleOwner,
                                lensFacing = uiState.lensFacing,
                                flashEnabled = uiState.flashEnabled
                            )
                        }
                    }
                )
            }

            // Controls
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = vm::toggleLens) {
                    Icon(Icons.Default.Cached, contentDescription = "Cambiar cámara")
                }

                FilledIconButton(
                    onClick = {
                        val uri = createOutputUri()
                        if (uri == null) {
                            vm.onCaptureFailed()
                            return@FilledIconButton
                        }

                        vm.onCaptureStarted()
                        val output = ImageCapture.OutputFileOptions
                            .Builder(context.contentResolver, uri, ContentValues())
                            .build()

                        controller.takePicture(
                            outputOptions = output,
                            onSuccess = {
                                vm.onCaptured(uri)
                            },
                            onError = {
                                vm.onCaptureFailed()
                            }
                        )
                    },
                    enabled = !uiState.isCapturing
                ) {
                    Icon(Icons.Default.PhotoCamera, contentDescription = "Capturar")
                }

                IconButton(onClick = vm::toggleFlash) {
                    Icon(
                        if (uiState.flashEnabled) Icons.Default.FlashOn else Icons.Default.FlashOff,
                        contentDescription = "Flash"
                    )
                }
            }

            Spacer(Modifier.height(6.dp))

            OutlinedButton(
                onClick = onBack,
                modifier = Modifier.fillMaxWidth()
            ) { Text("Volver") }
        }
    }
}