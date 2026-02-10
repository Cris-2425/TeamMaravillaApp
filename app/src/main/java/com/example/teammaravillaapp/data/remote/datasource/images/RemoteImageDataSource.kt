package com.example.teammaravillaapp.data.remote.datasource.images

import androidx.annotation.DrawableRes

/**
 * Fuente de datos remota para imágenes de productos o recetas.
 *
 * Define las operaciones básicas sobre el backend de imágenes:
 * - Verificar si existe una imagen.
 * - Subir una imagen desde un recurso drawable.
 * - Construir la URL pública de acceso a una imagen.
 */
interface RemoteImageDataSource {

    /**
     * Comprueba si la imagen con el identificador dado existe en el backend.
     *
     * @param id Identificador único de la imagen.
     * @return `true` si la imagen existe, `false` en caso contrario o si ocurre un error.
     */
    suspend fun exists(id: String): Boolean

    /**
     * Sube una imagen al backend a partir de un recurso drawable local.
     *
     * @param id Identificador único que se usará como nombre de la imagen.
     * @param drawableRes Recurso drawable de Android (`R.drawable.*`) que se subirá.
     */
    suspend fun uploadFromDrawable(
        id: String,
        @DrawableRes drawableRes: Int
    )

    /**
     * Construye la URL pública de la imagen correspondiente al ID dado.
     *
     * No realiza ninguna petición de red.
     *
     * @param id Identificador único de la imagen.
     * @return URL pública de la imagen.
     */
    fun buildPublicUrl(id: String): String
}