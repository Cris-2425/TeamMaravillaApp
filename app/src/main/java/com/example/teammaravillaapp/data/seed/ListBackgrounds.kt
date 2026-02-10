package com.example.teammaravillaapp.data.seed

import androidx.annotation.DrawableRes
import com.example.teammaravillaapp.R
import com.example.teammaravillaapp.model.ListBackground

/**
 * Resolver centralizado de fondos de listas.
 *
 * Motivo:
 * - Evitar repetir `when(label)` por toda la app.
 * - Mantener un único punto de verdad para mapear [ListBackground] -> drawable.
 *
 * Nota:
 * - Devuelve siempre un drawable válido (fallback) para que la UI nunca quede sin fondo.
 */
object ListBackgrounds {

    /**
     * Devuelve el drawable asociado a un [ListBackground].
     *
     * @param label Fondo elegido por el usuario.
     * Si es `null` o no coincide, se devuelve un fondo por defecto.
     *
     * @return id de recurso drawable (siempre válido).
     */
    @DrawableRes
    fun getBackgroundRes(label: ListBackground?): Int = when (label) {
        ListBackground.FONDO1 -> R.drawable.fondo_farmacia
        ListBackground.FONDO2 -> R.drawable.fondo_bbq
        ListBackground.FONDO3 -> R.drawable.fondo_desayuno
        ListBackground.FONDO4 -> R.drawable.fondo_limpieza
        else -> R.drawable.bg_app
    }
}