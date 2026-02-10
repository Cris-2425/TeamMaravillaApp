package com.example.teammaravillaapp.page.camera

import android.Manifest
import android.content.ContentValues
import android.net.Uri
import android.provider.MediaStore
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.ImageCapture
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.example.teammaravillaapp.R
import com.example.teammaravillaapp.ui.events.UiEvent
import com.example.teammaravillaapp.ui.theme.TeamMaravillaAppTheme
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Contenedor de la pantalla de cámara (capa de **presentación-contenerdor**).
 *
 * Esta función orquesta la integración con CameraX sin mezclar lógica de infraestructura
 * dentro de la UI pura. Centraliza:
 * - La solicitud del permiso de cámara y el envío del resultado al ViewModel.
 * - El *binding/unbinding* de CameraX (Preview + ImageCapture) mediante [CameraXController].
 * - La creación de una [Uri] de salida en [MediaStore] para persistir la foto capturada.
 * - La traducción de acciones de UI (botones/slider) a llamadas al controller o al ViewModel.
 *
 * La UI “pura” (dibujado del estado y callbacks) se delega en [CameraContent], lo que facilita
 * previews y pruebas unitarias de la presentación.
 *
 * @param listId Identificador de la lista a la que se asociará el ticket capturado.
 * Restricciones:
 * - Puede ser `null`/blank (por navegación o pantalla genérica), en cuyo caso
 *   la UI deshabilita la acción “Guardar ticket” (regla de negocio).
 * @param onBack Callback de navegación hacia atrás. Debe ser no nulo y sin efectos secundarios
 * costosos (se ejecuta en el hilo principal).
 * @param onUiEvent Consumidor de eventos one-shot (por ejemplo, snackbar). Debe ser no nulo.
 * Se recomienda que la capa superior lo consuma de forma idempotente.
 * @param vm ViewModel de la cámara. Por defecto se inyecta con Hilt vía [hiltViewModel],
 * pero puede “overridearse” en tests.
 *
 * @throws SecurityException Puede producirse si se intentan ejecutar operaciones de cámara sin permiso
 * efectivo (por ejemplo, estados inconsistentes del sistema). En esta implementación se evita
 * solicitando permiso antes de bindear, pero el sistema puede revocar permisos.
 * @throws IllegalArgumentException Puede producirse desde CameraX si el selector/lifecycle no es válido
 * o si se intenta bindear en un estado no soportado.
 * @throws IllegalStateException Puede producirse si el proveedor de cámara no está disponible
 * o el proceso de bind falla por estado del lifecycle.
 *
 * @see CameraContent UI pura (presentación).
 * @see CameraXController Encapsula operaciones CameraX (bind/flash/torch/zoom/captura).
 * @see CameraViewModel Fuente de verdad del estado de cámara y acciones de negocio.
 *
 * Ejemplo de uso:
 * {@code
 * CameraScreen(
 *   listId = navArgs.listId,
 *   onBack = navController::popBackStack,
 *   onUiEvent = { event -> handleUiEvent(event) }
 * )
 * }
 */
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
    ) { granted -> vm.onPermissionResult(granted) }

    LaunchedEffect(Unit) {
        permissionLauncher.launch(Manifest.permission.CAMERA)
    }

    var previewViewRef by remember { mutableStateOf<PreviewView?>(null) }

    // Re-bindea cuando cambie lente/permisos y solo si no hay foto capturada
    LaunchedEffect(uiState.hasPermission, uiState.lensFacing, uiState.capturedUri) {
        if (uiState.hasPermission && uiState.capturedUri == null) {
            previewViewRef?.let { pv ->
                controller.bind(
                    previewView = pv,
                    lifecycleOwner = lifecycleOwner,
                    lensFacing = uiState.lensFacing,
                    flashEnabled = uiState.flashEnabled,
                    onBound = { maxZoom ->
                        vm.onMaxZoomKnown(maxZoom)
                        // Ajuste defensivo del zoom actual al rango
                        vm.setZoomRatio(uiState.zoomRatio.coerceIn(1f, maxZoom.coerceAtLeast(1f)))
                    }
                )
            }
        }
    }

    LaunchedEffect(uiState.flashEnabled) {
        controller.updateFlash(uiState.flashEnabled)
    }

    // Torch (linterna continua)
    LaunchedEffect(uiState.torchEnabled) {
        controller.setTorch(uiState.torchEnabled)
    }

    // Zoom ratio
    LaunchedEffect(uiState.zoomRatio) {
        controller.setZoomRatio(uiState.zoomRatio)
    }

    DisposableEffect(Unit) {
        onDispose { controller.unbind() }
    }

    /**
     * Crea una URI de salida en MediaStore para almacenar la foto capturada.
     *
     * Motivo: evitar escribir directamente en rutas de archivo y delegar en el sistema
     * (compatibilidad con scoped storage).
     *
     * @return [Uri] válida si MediaStore puede crear el registro; `null` si la inserción falla.
     * @throws SecurityException si el sistema deniega acceso a MediaStore (raro, pero posible).
     */
    fun createOutputUri(): Uri? {
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

    Box(Modifier.fillMaxSize()) {

        CameraContent(
            uiState = uiState,
            onRequestPermission = { permissionLauncher.launch(Manifest.permission.CAMERA) },
            onToggleLens = vm::toggleLens,
            onToggleFlash = vm::toggleFlash,
            onToggleTorch = vm::toggleTorch,
            onZoomChange = vm::setZoomRatio,
            onCapture = {
                val uri = createOutputUri()
                if (uri == null) {
                    vm.onCaptureFailed()
                    return@CameraContent
                }

                vm.onCaptureStarted()

                val output = ImageCapture.OutputFileOptions
                    .Builder(context.contentResolver, uri, ContentValues())
                    .build()

                controller.takePicture(
                    outputOptions = output,
                    onSuccess = { vm.onCaptured(uri) },
                    onError = { vm.onCaptureFailed() }
                )
            },
            onRetake = vm::retake,
            onSaveReceipt = { vm.saveReceipt(listId) },
            onBack = onBack,
            canSaveReceipt = !listId.isNullOrBlank()
        )

        // Capa real de PreviewView (solo runtime)
        if (uiState.hasPermission && uiState.capturedUri == null) {
            Surface(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp, vertical = 64.dp)
                    .heightIn(min = 320.dp),
                shape = MaterialTheme.shapes.large,
                tonalElevation = 1.dp
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
                                flashEnabled = uiState.flashEnabled,
                                onBound = { maxZoom ->
                                    vm.onMaxZoomKnown(maxZoom)
                                    vm.setZoomRatio(uiState.zoomRatio.coerceIn(1f, maxZoom.coerceAtLeast(1f)))
                                }
                            )
                        }
                    }
                )
            }
        }
    }
}

/**
 * Preview de la UI de cámara sin permiso (estado típico inicial).
 *
 * Nota: en previews no se ejecuta CameraX real. Se usa [CameraContent] con `previewContent`.
 */
@Preview(showBackground = true)
@Composable
private fun PreviewCameraContent_NoPermission() {
    TeamMaravillaAppTheme {
        CameraContent(
            uiState = CameraUiState(hasPermission = false),
            onRequestPermission = {},
            onToggleLens = {},
            onToggleFlash = {},
            onToggleTorch = {},
            onZoomChange = {},
            onCapture = {},
            onRetake = {},
            onSaveReceipt = {},
            onBack = {},
            canSaveReceipt = false,
            previewContent = {
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.surfaceVariant) {}
            }
        )
    }
}

/**
 * Preview de la UI con permiso y modo preview (sin CameraX real).
 */
@Preview(showBackground = true)
@Composable
private fun PreviewCameraContent_WithPermission_PreviewMode() {
    TeamMaravillaAppTheme {
        CameraContent(
            uiState = CameraUiState(hasPermission = true, flashEnabled = true),
            onRequestPermission = {},
            onToggleLens = {},
            onToggleFlash = {},
            onToggleTorch = {},
            onZoomChange = {},
            onCapture = {},
            onRetake = {},
            onSaveReceipt = {},
            onBack = {},
            canSaveReceipt = true,
            previewContent = {
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.surfaceVariant) {
                    Column(
                        Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(stringResource(R.string.camera_preview_placeholder))
                    }
                }
            }
        )
    }
}

/**
 * Preview de la UI con torch y zoom activados.
 */
@Preview(showBackground = true)
@Composable
private fun PreviewCameraContent_WithTorchZoom() {
    TeamMaravillaAppTheme {
        CameraContent(
            uiState = CameraUiState(
                hasPermission = true,
                torchEnabled = true,
                zoomRatio = 2.0f,
                maxZoomRatio = 6.0f
            ),
            onRequestPermission = {},
            onToggleLens = {},
            onToggleFlash = {},
            onToggleTorch = {},
            onZoomChange = {},
            onCapture = {},
            onRetake = {},
            onSaveReceipt = {},
            onBack = {},
            canSaveReceipt = true,
            previewContent = {
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.surfaceVariant) {
                    Column(
                        Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(stringResource(R.string.camera_preview_placeholder))
                    }
                }
            }
        )
    }
}