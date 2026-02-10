package com.example.teammaravillaapp.page.camera

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cached
import androidx.compose.material.icons.filled.FlashOff
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material.icons.filled.FlashlightOff
import androidx.compose.material.icons.filled.FlashlightOn
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material3.Button
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.teammaravillaapp.R
import com.example.teammaravillaapp.component.GeneralBackground

/**
 * Pantalla de cámara (UI pura / capa de **presentación**).
 *
 * Renderiza el estado de [CameraUiState] y expone callbacks para que el contenedor ([CameraScreen])
 * ejecute la lógica de permisos, CameraX y guardado.
 *
 * Se diseña así por dos motivos:
 * 1) **Reutilización y testabilidad**: esta UI se puede previsualizar sin depender de hardware/cámara.
 * 2) **Separación de responsabilidades**: evita recomposiciones “peligrosas” que rompan CameraX.
 *
 * @param uiState Estado inmutable que describe el “qué” se debe renderizar:
 * - permisos, capturando/guardando, flash/torch, zoom y [android.net.Uri] capturada.
 * Restricciones:
 * - `zoomRatio` debe estar en el rango `[1f..maxZoomRatio]` (se coacciona defensivamente en UI).
 * - `maxZoomRatio` debe ser >= 1f.
 * @param onRequestPermission Acción para solicitar permiso cuando no está concedido.
 * Debe ser idempotente (se puede pulsar varias veces).
 * @param onToggleLens Acción para alternar lente trasera/delantera.
 * @param onToggleFlash Acción para alternar flash (aplica a captura, no a linterna continua).
 * @param onToggleTorch Acción para alternar la linterna continua (torch).
 * @param onZoomChange Callback al mover el slider. Restricciones:
 * - Se recibe un `Float` en rango `[1f..maxZoomRatio]`.
 * - Se recomienda que la capa superior lo “clamp” también por seguridad.
 * @param onCapture Acción para capturar una foto. Debe manejar:
 * - crear URI de salida
 * - disparar CameraX
 * - actualizar estado en VM
 * @param onRetake Acción para descartar la captura y volver al preview.
 * @param onSaveReceipt Acción para guardar el ticket asociado a una lista.
 * @param onBack Acción de navegación hacia atrás.
 * @param canSaveReceipt Indica si “Guardar ticket” está permitido (regla de negocio).
 * Normalmente es `true` solo si existe `listId` válido.
 * @param previewContent Contenido opcional para reemplazar el preview real en @Preview.
 * - En runtime debería ser `null` para que el contenedor pinte el [androidx.camera.view.PreviewView].
 *
 * @throws IllegalArgumentException No se lanza directamente aquí, pero puede propagarse si el caller
 * pasa rangos inválidos (por ejemplo, `maxZoomRatio < 1f`) y luego usa esos valores en CameraX.
 *
 * @see CameraScreen Contenedor que implementa permisos + CameraX.
 * @see CameraUiState Modelo de estado.
 *
 * Ejemplo de uso:
 * {@code
 * CameraContent(
 *   uiState = uiState,
 *   onToggleTorch = viewModel::toggleTorch,
 *   onZoomChange = viewModel::setZoomRatio,
 *   onCapture = { captureWithController() },
 *   canSaveReceipt = !listId.isNullOrBlank()
 * )
 * }
 */
@Composable
fun CameraContent(
    uiState: CameraUiState,
    onRequestPermission: () -> Unit,
    onToggleLens: () -> Unit,
    onToggleFlash: () -> Unit,
    onToggleTorch: () -> Unit,
    onZoomChange: (Float) -> Unit,
    onCapture: () -> Unit,
    onRetake: () -> Unit,
    onSaveReceipt: () -> Unit,
    onBack: () -> Unit,
    canSaveReceipt: Boolean,
    previewContent: (@Composable () -> Unit)? = null
) {
    val title = stringResource(R.string.camera_title)
    val permissionMissing = stringResource(R.string.camera_permission_missing)
    val permissionGrant = stringResource(R.string.camera_permission_grant)

    val retake = stringResource(R.string.camera_retake)
    val saveReceipt = stringResource(R.string.camera_save_receipt)
    val saving = stringResource(R.string.camera_saving)

    val cdToggleLens = stringResource(R.string.camera_cd_toggle_lens)
    val cdCapture = stringResource(R.string.camera_cd_capture)
    val cdFlash = stringResource(R.string.camera_cd_flash)
    val cdTorch = stringResource(R.string.camera_cd_torch)

    val zoomLabel = stringResource(R.string.camera_zoom_label)
    val zoomText = stringResource(R.string.camera_zoom_value, uiState.zoomRatio)

    val back = stringResource(R.string.common_back)

    GeneralBackground(overlayAlpha = 0.18f) {
        Column(
            Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(text = title, style = MaterialTheme.typography.titleLarge)

            // Sin permiso
            if (!uiState.hasPermission) {
                Surface(
                    shape = MaterialTheme.shapes.medium,
                    tonalElevation = 1.dp,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        Modifier.padding(12.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Text(permissionMissing)
                        Button(onClick = onRequestPermission) { Text(permissionGrant) }
                    }
                }

                OutlinedButton(onClick = onBack, modifier = Modifier.fillMaxWidth()) { Text(back) }
                return@GeneralBackground
            }

            // Foto capturada
            if (uiState.capturedUri != null) {
                Surface(
                    shape = MaterialTheme.shapes.large,
                    tonalElevation = 1.dp,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    AsyncImage(
                        model = uiState.capturedUri,
                        contentDescription = stringResource(R.string.camera_captured_image_cd),
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
                        onClick = onRetake,
                        modifier = Modifier.weight(1f),
                        enabled = !uiState.isSaving
                    ) { Text(retake) }

                    Button(
                        onClick = onSaveReceipt,
                        modifier = Modifier.weight(1f),
                        enabled = !uiState.isSaving && canSaveReceipt
                    ) { Text(if (uiState.isSaving) saving else saveReceipt) }
                }

                OutlinedButton(onClick = onBack, modifier = Modifier.fillMaxWidth()) { Text(back) }
                return@GeneralBackground
            }

            // Hueco visual donde el contenedor superpone PreviewView (runtime) o placeholder (preview)
            Surface(
                shape = MaterialTheme.shapes.large,
                tonalElevation = 1.dp,
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 320.dp)
            ) {
                if (previewContent != null) previewContent()
            }

            // Controles (fila 1): lens / capture / flash / torch
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onToggleLens) {
                    Icon(Icons.Default.Cached, contentDescription = cdToggleLens)
                }

                FilledIconButton(onClick = onCapture, enabled = !uiState.isCapturing) {
                    Icon(Icons.Default.PhotoCamera, contentDescription = cdCapture)
                }

                IconButton(onClick = onToggleFlash) {
                    Icon(
                        if (uiState.flashEnabled) Icons.Default.FlashOn else Icons.Default.FlashOff,
                        contentDescription = cdFlash
                    )
                }

                IconButton(onClick = onToggleTorch) {
                    Icon(
                        if (uiState.torchEnabled) Icons.Default.FlashlightOn else Icons.Default.FlashlightOff,
                        contentDescription = cdTorch
                    )
                }
            }

            // Zoom (fila 2)
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(zoomLabel, style = MaterialTheme.typography.titleSmall)
                    Text(zoomText, style = MaterialTheme.typography.bodyMedium)
                }

                Slider(
                    value = uiState.zoomRatio.coerceIn(1f, uiState.maxZoomRatio),
                    onValueChange = onZoomChange,
                    valueRange = 1f..uiState.maxZoomRatio.coerceAtLeast(1f),
                    enabled = uiState.maxZoomRatio > 1f
                )
            }

            OutlinedButton(onClick = onBack, modifier = Modifier.fillMaxWidth()) { Text(back) }
        }
    }
}