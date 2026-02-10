package com.example.teammaravillaapp.ui.events

import androidx.annotation.StringRes

/**
 * Resultado de una operación de UI/caso de uso.
 *
 * Motivo:
 * - Representar “loading / éxito / error” de forma tipada y consistente.
 * - Permitir que la UI muestre mensajes localizables mediante `@StringRes`.
 *
 * @param T Tipo del dato devuelto en caso de éxito.
 */
sealed interface UiResult<out T> {

    /**
     * Operación en curso.
     */
    data object Loading : UiResult<Nothing>

    /**
     * Operación completada con éxito.
     *
     * @param data Dato resultante.
     */
    data class Success<T>(val data: T) : UiResult<T>

    /**
     * Operación fallida.
     *
     * @param messageResId Recurso de string del mensaje de error (localizable).
     * @param cause Excepción técnica opcional (logging/diagnóstico).
     */
    data class Error(
        @StringRes val messageResId: Int,
        val cause: Throwable? = null
    ) : UiResult<Nothing>
}