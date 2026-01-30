package com.example.teammaravillaapp.page.camera

import android.net.Uri

data class CameraUiState(
    val pickedUri: Uri? = null,
    val isSaving: Boolean = false
)