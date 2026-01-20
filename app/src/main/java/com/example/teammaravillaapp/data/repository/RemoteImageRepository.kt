package com.example.teammaravillaapp.data.repository

import android.annotation.SuppressLint
import android.content.Context
import androidx.annotation.DrawableRes
import com.example.teammaravillaapp.model.ProductImageExt
import com.example.teammaravillaapp.network.api.ImageApi
import dagger.hilt.android.qualifiers.ApplicationContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RemoteImageRepository @Inject constructor(
    @ApplicationContext private val appContext: Context,
    private val api: ImageApi
) {
    // Cambia el puerto si tu API usa otro
    fun buildPublicUrl(id: String): String = "http://10.0.2.2:5131/images/$id"

    /**
     * Sube una imagen desde res/drawable usando multipart.
     *
     * Usa ProductImageExt.extFor(id) para enviar:
     * - filename correcto (id.jpg / id.jpeg / id.png)
     * - MIME correcto (image/jpeg o image/png)
     */
    @SuppressLint("ResourceType")
    suspend fun uploadFromDrawable(
        id: String,
        @DrawableRes drawableRes: Int
    ) {
        val ext = ProductImageExt.extFor(id) // "jpg" / "jpeg" / "png"

        val mime = when (ext.lowercase()) {
            "jpg", "jpeg" -> "image/jpeg"
            "png" -> "image/png"
            else -> "application/octet-stream"
        }.toMediaType()

        val bytes = appContext.resources.openRawResource(drawableRes).use { it.readBytes() }

        val body = bytes.toRequestBody(mime)
        val part = MultipartBody.Part.createFormData(
            name = "file",
            filename = "$id.$ext",
            body = body
        )

        // Endpoint esperado: POST /images/{id} multipart (file)
        api.uploadImage(id = id, file = part)
    }
}