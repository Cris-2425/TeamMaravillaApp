package com.example.teammaravillaapp.model

import androidx.annotation.StringRes
import com.example.teammaravillaapp.R

/**
 * Tipos de vista disponibles para mostrar listas en la UI.
 *
 * Cada tipo de vista tiene un string resource asociado, que puede ser usado
 * para mostrar etiquetas de usuario (por ejemplo, en ajustes o menús de selección).
 *
 * Ejemplo de uso:
 * ```kotlin
 * val type: ListViewType = ListViewType.BUBBLES
 * val label = context.getString(type.labelRes)
 * ```
 *
 * @property labelRes Recurso de string que describe la vista de lista en la UI.
 */
enum class ListViewType(@StringRes val labelRes: Int) {
    BUBBLES(R.string.list_view_bubbles),
    LIST(R.string.list_view_list),
    COMPACT(R.string.list_view_compact)
}