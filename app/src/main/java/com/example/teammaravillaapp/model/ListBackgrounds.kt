package com.example.teammaravillaapp.model

import androidx.annotation.DrawableRes
import com.example.teammaravillaapp.R

object ListBackgrounds {

    @DrawableRes
    fun getBackgroundRes(label: ListBackground?): Int = when (label) {
        ListBackground.FONDO1 -> R.drawable.fondo_farmacia
        ListBackground.FONDO2 -> R.drawable.fondo_bbq
        ListBackground.FONDO3 -> R.drawable.fondo_desayuno
        ListBackground.FONDO4 -> R.drawable.fondo_limpieza
        else -> R.drawable.background_app_final
    }
}