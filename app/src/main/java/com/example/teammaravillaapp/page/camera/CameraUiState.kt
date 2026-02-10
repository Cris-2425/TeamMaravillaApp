package com.example.teammaravillaapp.page.camera

import android.net.Uri

/**
 * Estado de UI para la pantalla de cámara basada en CameraX.
 *
 * Representa de forma declarativa lo que la pantalla debe mostrar y habilitar:
 * - permisos de cámara
 * - captura y guardado
 * - configuración de lente, flash/torch y zoom
 * - URI de la foto capturada (si existe)
 *
 * Este estado está pensado para ser la única fuente de verdad (single source of truth)
 * consumida por la UI de Compose.
 *
 * Ejemplo de uso:
 * {@code
 * val uiState by vm.uiState.collectAsState()
 * if (uiState.capturedUri != null) { ... }
 * }
 *
 * @property hasPermission Indica si la app dispone de permiso de cámara. Si es false, se muestra el flujo de permisos.
 * @property isCapturing True mientras se está realizando una captura. Se usa para deshabilitar botones y evitar dobles taps.
 * @property isSaving True mientras se está persistiendo/registrando el ticket (URI) asociado a una lista.
 * @property lensFacing Lente activa: [LENS_BACK] o [LENS_FRONT]. No debe tomar otros valores.
 * @property flashEnabled Flash de captura (foto). No es equivalente a torch.
 * @property torchEnabled Linterna continua. Requiere que el controlador de cámara lo aplique.
 * @property zoomRatio Factor de zoom actual. 1f = normal, 2f = x2. Debe ser >= 1f.
 * @property maxZoomRatio Zoom máximo reportado por la cámara. Debe ser >= 1f.
 * @property capturedUri URI de la última foto capturada. Si no es null, la pantalla muestra previsualización y acciones.
 */
data class CameraUiState(
    val hasPermission: Boolean = false,
    val isCapturing: Boolean = false,
    val isSaving: Boolean = false,
    val lensFacing: Int = LENS_BACK,
    val flashEnabled: Boolean = false,
    val torchEnabled: Boolean = false,
    val zoomRatio: Float = 1f,
    val maxZoomRatio: Float = 4f,
    val capturedUri: Uri? = null
) {
    companion object {
        /** Lente trasera (valor estable interno para la UI). */
        const val LENS_BACK = 0

        /** Lente frontal (valor estable interno para la UI). */
        const val LENS_FRONT = 1
    }
}