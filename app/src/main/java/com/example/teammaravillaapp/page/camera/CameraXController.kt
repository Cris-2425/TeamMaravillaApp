package com.example.teammaravillaapp.page.camera

import android.content.Context
import androidx.camera.core.Camera
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import java.util.concurrent.Executor

/**
 * Controlador mínimo para encapsular CameraX (bind, flash, torch, zoom y captura).
 *
 * Este wrapper existe para **aislar** la lógica de infraestructura de CameraX y evitar que la UI
 * (Compose) tenga que gestionar directamente:
 * - Proveedor de cámara ([ProcessCameraProvider])
 * - UseCases (Preview, ImageCapture)
 * - Lifecycle y rebindings
 *
 * Beneficios:
 * - Reduce riesgo de errores por recomposición / lifecycle.
 * - Simplifica la API consumida por [CameraScreen] (y eventualmente por tests).
 *
 * @param context Contexto (idealmente Application/Activity) usado para obtener el proveedor de cámara
 * y el executor principal. Restricción: no debe ser nulo.
 *
 * @see CameraScreen Contenedor que usa este controlador.
 * @see CameraUiState Estado que alimenta flash/torch/zoom.
 *
 * Ejemplo de uso:
 * {@code
 * val controller = CameraXController(context)
 * controller.bind(previewView, lifecycleOwner, CameraUiState.LENS_BACK, flashEnabled = false)
 * controller.setTorch(true)
 * controller.setZoomRatio(2.0f)
 * }
 */
class CameraXController(
    private val context: Context
) {
    private var cameraProvider: ProcessCameraProvider? = null
    private var imageCapture: ImageCapture? = null
    private var camera: Camera? = null

    private val mainExecutor: Executor = ContextCompat.getMainExecutor(context)

    /**
     * Realiza el *binding* de CameraX al [lifecycleOwner] con un [PreviewView] como destino visual.
     *
     * Internamente:
     * - Obtiene [ProcessCameraProvider].
     * - Construye use case de [Preview] y de [ImageCapture].
     * - Selecciona lente (front/back).
     * - Llama a `unbindAll()` antes de bindear para evitar duplicados.
     *
     * @param previewView Vista de CameraX donde se dibuja el preview. Restricción: no nula.
     * @param lifecycleOwner Owner del lifecycle (por ejemplo, Activity o NavBackStackEntry). Restricción: no nulo.
     * @param lensFacing Lente deseada. Restricciones:
     * - Usar [CameraUiState.LENS_BACK] o [CameraUiState.LENS_FRONT].
     * - Cualquier otro valor puede causar selección inválida.
     * @param flashEnabled Indica si el modo flash para captura debe estar activo.
     * Nota: el torch (linterna continua) se gestiona con [setTorch].
     * @param onBound Callback opcional invocado cuando la cámara queda enlazada.
     * Se usa típicamente para publicar `maxZoomRatio` al ViewModel.
     *
     * @throws SecurityException Si la app no dispone de permiso de cámara en el momento del bind.
     * @throws IllegalArgumentException Si el selector de cámara es inválido o el lensFacing no es soportado.
     * @throws IllegalStateException Si CameraProvider no puede completar el bind por estado del lifecycle o recursos.
     *
     * Ejemplo:
     * {@code
     * controller.bind(
     *   previewView = pv,
     *   lifecycleOwner = lifecycleOwner,
     *   lensFacing = CameraUiState.LENS_BACK,
     *   flashEnabled = false,
     *   onBound = { maxZoom -> println(maxZoom) }
     * )
     * }
     */
    fun bind(
        previewView: PreviewView,
        lifecycleOwner: LifecycleOwner,
        lensFacing: Int,
        flashEnabled: Boolean,
        onBound: ((maxZoomRatio: Float) -> Unit)? = null
    ) {
        val providerFuture = ProcessCameraProvider.getInstance(context)
        providerFuture.addListener({
            cameraProvider = providerFuture.get()

            val preview = Preview.Builder().build().also {
                it.setSurfaceProvider(previewView.surfaceProvider)
            }

            imageCapture = ImageCapture.Builder()
                .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
                .build()
                .also { cap ->
                    cap.flashMode =
                        if (flashEnabled) ImageCapture.FLASH_MODE_ON else ImageCapture.FLASH_MODE_OFF
                }

            val selector = CameraSelector.Builder()
                .requireLensFacing(
                    if (lensFacing == CameraUiState.LENS_FRONT) CameraSelector.LENS_FACING_FRONT
                    else CameraSelector.LENS_FACING_BACK
                )
                .build()

            cameraProvider?.unbindAll()
            camera = cameraProvider?.bindToLifecycle(
                lifecycleOwner,
                selector,
                preview,
                imageCapture
            )

            onBound?.invoke(getMaxZoomRatioOrDefault())
        }, mainExecutor)
    }

    /**
     * Actualiza el modo flash de la captura fotográfica.
     *
     * Motivo: permitir que la UI cambie flash sin rebind completo.
     *
     * @param flashEnabled `true` para activar flash en captura, `false` para desactivarlo.
     * @throws IllegalStateException Si [imageCapture] aún no existe (por ejemplo, bind no realizado).
     *
     * Ejemplo:
     * {@code
     * controller.updateFlash(true)
     * }
     */
    fun updateFlash(flashEnabled: Boolean) {
        imageCapture?.flashMode =
            if (flashEnabled) ImageCapture.FLASH_MODE_ON else ImageCapture.FLASH_MODE_OFF
    }

    /**
     * Activa o desactiva el **torch** (linterna continua).
     *
     * Diferencia clave:
     * - Flash: se usa en el momento de disparar la foto.
     * - Torch: ilumina de forma continua mientras está activo.
     *
     * @param enabled `true` para encender la linterna, `false` para apagarla.
     * @throws IllegalStateException Si no hay cámara enlazada todavía (camera == null).
     *
     * Ejemplo:
     * {@code
     * controller.setTorch(true)
     * }
     */
    fun setTorch(enabled: Boolean) {
        camera?.cameraControl?.enableTorch(enabled)
    }

    /**
     * Ajusta el ratio de zoom de la cámara.
     *
     * @param ratio Ratio de zoom. Restricciones:
     * - 1.0f = sin zoom.
     * - Debe estar en el rango [1f..maxZoomRatio] soportado por la cámara.
     * @throws IllegalArgumentException Si el ratio está fuera del rango soportado (puede ser rechazado por CameraX).
     * @throws IllegalStateException Si no hay cámara enlazada todavía.
     *
     * Ejemplo:
     * {@code
     * controller.setZoomRatio(2.0f) // x2
     * }
     */
    fun setZoomRatio(ratio: Float) {
        camera?.cameraControl?.setZoomRatio(ratio)
    }

    /**
     * Obtiene el máximo ratio de zoom soportado por la cámara enlazada.
     *
     * Si la cámara todavía no está enlazada o no hay estado de zoom disponible,
     * devuelve un valor por defecto para mantener la UI funcional.
     *
     * @param default Valor fallback cuando no se puede leer el máximo real.
     * Restricción: debería ser >= 1f.
     * @return Máximo ratio de zoom (>= 1f).
     *
     * Ejemplo:
     * {@code
     * val maxZoom = controller.getMaxZoomRatioOrDefault()
     * }
     */
    fun getMaxZoomRatioOrDefault(default: Float = 4f): Float {
        val info = camera?.cameraInfo ?: return default
        val state = info.zoomState.value ?: return default
        return state.maxZoomRatio.coerceAtLeast(1f)
    }

    /**
     * Realiza la captura de imagen y guarda el resultado usando [outputOptions].
     *
     * @param outputOptions Opciones de salida (por ejemplo, MediaStore + Uri). Restricción: no nulo.
     * @param onSuccess Callback invocado si la foto se guarda correctamente. No debe bloquear el hilo principal.
     * @param onError Callback invocado si falla la captura o el guardado. No debe bloquear el hilo principal.
     *
     * @throws IllegalStateException Si [imageCapture] no está inicializado (bind no realizado).
     * @throws SecurityException Si el sistema deniega escritura/lectura sobre el destino (ej. MediaStore).
     *
     * Ejemplo:
     * {@code
     * controller.takePicture(
     *   outputOptions = output,
     *   onSuccess = { println("OK") },
     *   onError = { e -> println("Error: $e") }
     * )
     * }
     */
    fun takePicture(
        outputOptions: ImageCapture.OutputFileOptions,
        onSuccess: (ImageCapture.OutputFileResults) -> Unit,
        onError: (ImageCaptureException) -> Unit
    ) {
        val cap = imageCapture ?: return
        cap.takePicture(
            outputOptions,
            mainExecutor,
            object : ImageCapture.OnImageSavedCallback {
                override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                    onSuccess(outputFileResults)
                }

                override fun onError(exception: ImageCaptureException) {
                    onError(exception)
                }
            }
        )
    }

    /**
     * Desenlaza todos los use cases de CameraX y libera referencias internas.
     *
     * Motivo: evitar fugas de memoria y asegurar que el lifecycle libera recursos de cámara
     * al salir de la pantalla.
     *
     * @throws IllegalStateException Puede ocurrir si CameraProvider está en un estado inconsistente.
     *
     * Ejemplo:
     * {@code
     * controller.unbind()
     * }
     */
    fun unbind() {
        cameraProvider?.unbindAll()
        camera = null
    }

    fun takePictureSafe(
        outputOptions: ImageCapture.OutputFileOptions,
        onSuccess: (ImageCapture.OutputFileResults) -> Unit,
        onError: (ImageCaptureException) -> Unit
    ): Boolean {
        val cap = imageCapture ?: return false

        cap.takePicture(
            outputOptions,
            mainExecutor,
            object : ImageCapture.OnImageSavedCallback {
                override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                    onSuccess(outputFileResults)
                }

                override fun onError(exception: ImageCaptureException) {
                    onError(exception)
                }
            }
        )
        return true
    }
}