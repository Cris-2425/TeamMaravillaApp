package com.example.teammaravillaapp.model

import androidx.annotation.StringRes
import com.example.teammaravillaapp.R

/**
 * Claves de nombres de listas sugeridas (**SuggestedListName**).
 *
 * Este enum se utiliza como constantes de referencia para listas sugeridas.
 * El texto real se obtiene en la UI mediante `stringResource(labelRes)`.
 *
 * Ejemplo de uso:
 * ```kotlin
 * val weeklyShopName = SuggestedListName.SUGGESTED_WEEKLY_SHOP
 * val labelText = stringResource(weeklyShopName.labelRes)
 * ```
 *
 * @property labelRes Recurso de texto asociado a la lista sugerida (`R.string.*`).
 */
enum class SuggestedListName(@StringRes val labelRes: Int) {
    SUGGESTED_WEEKLY_SHOP(R.string.suggested_list_weekly_shop),
    SUGGESTED_BBQ(R.string.suggested_list_bbq),
    SUGGESTED_BREAKFAST(R.string.suggested_list_breakfast),
    SUGGESTED_CLEANING(R.string.suggested_list_cleaning)
}