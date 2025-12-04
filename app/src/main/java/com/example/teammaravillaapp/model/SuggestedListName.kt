package com.example.teammaravillaapp.model

import androidx.annotation.StringRes
import com.example.teammaravillaapp.R

/**
 * Claves de nombres de listas sugeridas.
 *
 * Se usa solo como ENUM de constantes; el texto real se
 * resuelve en las pantallas con `stringResource(labelRes)`.
 */
enum class SuggestedListName(@StringRes val labelRes: Int) {
    SUGGESTED_WEEKLY_SHOP(R.string.suggested_list_weekly_shop),
    SUGGESTED_BBQ(R.string.suggested_list_bbq),
    SUGGESTED_BREAKFAST(R.string.suggested_list_breakfast),
    SUGGESTED_CLEANING(R.string.suggested_list_cleaning)
}