package com.example.teammaravillaapp.page.camera

import android.content.Context
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import java.util.concurrent.Executor

class CameraXController(
    private val context: Context
) {
    private var cameraProvider: ProcessCameraProvider? = null
    private var imageCapture: ImageCapture? = null
    private var camera: Camera? = null

    private val mainExecutor: Executor = ContextCompat.getMainExecutor(context)

    fun bind(
        previewView: PreviewView,
        lifecycleOwner: LifecycleOwner,
        lensFacing: Int,
        flashEnabled: Boolean
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
                    cap.flashMode = if (flashEnabled) ImageCapture.FLASH_MODE_ON else ImageCapture.FLASH_MODE_OFF
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
        }, mainExecutor)
    }

    fun updateFlash(flashEnabled: Boolean) {
        imageCapture?.flashMode = if (flashEnabled) ImageCapture.FLASH_MODE_ON else ImageCapture.FLASH_MODE_OFF
    }

    /** Torch = linterna continua (no es flash de foto). */
    fun setTorch(enabled: Boolean) {
        camera?.cameraControl?.enableTorch(enabled)
    }

    /** zoomRatio típico: 1.0 = normal, 2.0 = x2 */
    fun setZoomRatio(ratio: Float) {
        camera?.cameraControl?.setZoomRatio(ratio)
    }

    fun getMaxZoomRatioOrDefault(default: Float = 4f): Float {
        val info = camera?.cameraInfo ?: return default
        val state = info.zoomState.value ?: return default
        return state.maxZoomRatio.coerceAtLeast(1f)
    }

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

    fun unbind() {
        cameraProvider?.unbindAll()
        camera = null
    }
}