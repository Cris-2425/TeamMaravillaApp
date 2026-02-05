package com.example.teammaravillaapp.data.remote.datasource.images

import android.annotation.SuppressLint
import android.content.Context
import androidx.annotation.DrawableRes
import com.example.teammaravillaapp.data.remote.api.ImageApi
import com.example.teammaravillaapp.data.seed.ProductImageExt
import dagger.hilt.android.qualifiers.ApplicationContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RemoteImageDataSourceImpl @Inject constructor(
    @ApplicationContext private val appContext: Context,
    private val api: ImageApi
) : RemoteImageDataSource {

    private val baseUrl = "http://10.0.2.2:5131"

    override fun buildPublicUrl(id: String): String =
        "$baseUrl/images/$id"

    override suspend fun exists(id: String): Boolean =
        runCatching { api.getImage(id).isSuccessful }
            .getOrDefault(false)

    override suspend fun uploadFromDrawable(
        id: String,
        drawableRes: Int
    ) {
        val ext = ProductImageExt.extFor(id)

        val mime = when (ext.lowercase()) {
            "jpg", "jpeg" -> "image/jpeg"
            "png" -> "image/png"
            else -> "application/octet-stream"
        }.toMediaType()

        val bytes = appContext.resources
            .openRawResource(drawableRes)
            .use { it.readBytes() }

        val body = bytes.toRequestBody(mime)

        val part = MultipartBody.Part.createFormData(
            name = "file",
            filename = "$id.$ext",
            body = body
        )

        api.uploadImage(id = id, file = part)
    }
}