package com.example.teammaravillaapp.util

import retrofit2.HttpException
import retrofit2.Response

/**
 * Extrae el `body()` de una respuesta HTTP exitosa o lanza una excepción con contexto.
 *
 * ### Por qué existe
 * Evita repetir el patrón `if (isSuccessful) ... else ...` y unifica el tipo de error:
 * - respuestas no exitosas → [HttpException]
 * - respuesta exitosa sin body → [IllegalStateException]
 *
 * @return El `body()` no nulo.
 *
 * @throws HttpException Si la respuesta no es exitosa.
 * @throws IllegalStateException Si `isSuccessful` pero el cuerpo es `null`.
 */
fun <T> Response<T>.bodyOrThrow(): T {
    if (isSuccessful) {
        return body() ?: throw IllegalStateException("Empty body with HTTP ${code()}")
    }
    throw HttpException(this)
}

/**
 * Variante para endpoints que devuelven listas.
 *
 * ### Semántica
 * - Si `isSuccessful` y el body es `null`, se degrada a `emptyList()` (contrato tolerante).
 * - Si no es exitosa, lanza [HttpException] con la respuesta original.
 *
 * @return Lista devuelta por el backend o vacía si el body es `null` en una respuesta exitosa.
 *
 * @throws HttpException Si la respuesta no es exitosa.
 */
fun <T> Response<List<T>>.listBodyOrEmptyOnSuccess(): List<T> {
    if (isSuccessful) return body().orEmpty()
    throw HttpException(this)
}