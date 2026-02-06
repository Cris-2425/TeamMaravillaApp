package com.example.teammaravillaapp.page.camera

import android.net.Uri

data class CameraUiState(
    val hasPermission: Boolean = false,
    val isCapturing: Boolean = false,
    val isSaving: Boolean = false,
    val lensFacing: Int = LENS_BACK,

    // Flash (foto)
    val flashEnabled: Boolean = false,

    // Torch (linterna continua)
    val torchEnabled: Boolean = false,

    // Zoom ratio
    val zoomRatio: Float = 1f,
    val maxZoomRatio: Float = 4f,

    val capturedUri: Uri? = null
){
    companion object {
        const val LENS_BACK = 0
        const val LENS_FRONT = 1
    }
}