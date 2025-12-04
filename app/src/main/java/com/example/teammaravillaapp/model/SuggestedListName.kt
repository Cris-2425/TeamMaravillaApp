package com.example.teammaravillaapp.model

import androidx.annotation.StringRes
import com.example.teammaravillaapp.R

/**
 * Representa nombres de listas sugeridas usando recursos de strings.
 *
 * Permite obtener:
 * - El recurso del string ([labelRes])
 * - El string final ([label]) usando [AppStrings]
 *
 * Esto evita escribir textos fijos en la UI y mantiene la app localizable.
 */
enum class SuggestedListName(@StringRes val labelRes: Int) {

    WEEKLY_SHOP(R.string.suggested_list_weekly_shop),
    BBQ(R.string.suggested_list_bbq),
    BREAKFAST(R.string.suggested_list_breakfast),
    CLEANING(R.string.suggested_list_cleaning);

    /** Texto legible de la lista sugerida. */
    val label: String
        get() = AppStrings.get(labelRes)
}