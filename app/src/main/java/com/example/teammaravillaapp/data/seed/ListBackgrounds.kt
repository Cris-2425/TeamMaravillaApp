package com.example.teammaravillaapp.data.seed

import androidx.annotation.DrawableRes
import com.example.teammaravillaapp.R
import com.example.teammaravillaapp.model.ListBackground

/**
 * Resolve de recursos para los fondos de lista.
 *
 * Centraliza el mapeo enum → drawable para no repetir
 * `when` por toda la app y tener un “single source of truth”.
 */
object ListBackgrounds {

    /**
     * Devuelve el drawable del fondo asociado a [label].
     *
     * Si [label] es `null` o no coincide con ningún caso, se devuelve
     * un fondo por defecto para que la UI nunca quede en blanco.
     *
     * @param label fondo elegido por el usuario (enum) o `null`.
     * @return id de recurso drawable siempre válido.
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
