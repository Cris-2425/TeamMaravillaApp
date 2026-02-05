package com.example.teammaravillaapp.data.remote.datasource.images

interface RemoteImageDataSource {

    suspend fun exists(id: String): Boolean

    suspend fun uploadFromDrawable(
        id: String,
        drawableRes: Int
    )

    fun buildPublicUrl(id: String): String
}