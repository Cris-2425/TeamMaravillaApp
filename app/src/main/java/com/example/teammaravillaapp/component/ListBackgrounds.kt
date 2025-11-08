package com.example.teammaravillaapp.component

import androidx.annotation.DrawableRes
import com.example.teammaravillaapp.R

object ListBackgrounds {
    const val Fondo1 = "Fondo1"
    const val Fondo2 = "Fondo2"
    const val Fondo3 = "Fondo3"
    const val Fondo4 = "Fondo4"

    @DrawableRes
    fun getBackgroundRes(label: String): Int? = when (label) {
        Fondo1 -> R.drawable.fondo_farmacia
        Fondo2 -> R.drawable.fondo_bbq
        Fondo3 -> R.drawable.fondo_desayuno
        Fondo4 -> R.drawable.fondo_limpieza
        else   -> null
    }
}
