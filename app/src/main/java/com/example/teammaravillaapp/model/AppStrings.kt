package com.example.teammaravillaapp.model

import android.content.Context
import androidx.annotation.StringRes

/**
 * Proveedor global seguro para obtener strings fuera de Compose.
 */
object AppStrings {

    private lateinit var appContext: Context

    fun init(context: Context) {
        appContext = context.applicationContext
    }

    fun get(@StringRes resId: Int): String =
        appContext.getString(resId)
}