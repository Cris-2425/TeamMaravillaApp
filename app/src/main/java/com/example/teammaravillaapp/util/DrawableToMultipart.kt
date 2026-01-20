package com.example.teammaravillaapp.network.util

import android.annotation.SuppressLint
import android.content.Context
import androidx.annotation.DrawableRes
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

/**
 * Convierte un drawable (R.drawable.xxx) a MultipartBody.Part.
 * Soporta jpg/jpeg/png según extensión.
 */
@SuppressLint("ResourceType")
fun drawableToMultipart(
    context: Context,
    @DrawableRes drawableRes: Int,
    filenameWithExt: String, // ej: "aceite.jpg" / "manzana.png"
): MultipartBody.Part {
    val file = File(context.cacheDir, filenameWithExt)

    context.resources.openRawResource(drawableRes).use { input ->
        file.outputStream().use { output ->
            input.copyTo(output)
        }
    }

    val contentType = when (file.extension.lowercase()) {
        "jpg", "jpeg" -> "image/jpeg"
        "png" -> "image/png"
        else -> "application/octet-stream"
    }.toMediaType()

    val body = file.asRequestBody(contentType)
    return MultipartBody.Part.createFormData("file", file.name, body)
}