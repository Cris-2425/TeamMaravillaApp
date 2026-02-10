package com.example.teammaravillaapp.model

import androidx.annotation.StringRes
import com.example.teammaravillaapp.R

/**
 * Representa las diferentes **opciones del perfil de usuario** disponibles
 * dentro de la aplicación.
 *
 * Cada opción del perfil está asociada a:
 * - una clave interna estable (el propio valor del enum),
 * - un recurso de texto visible para el usuario (`labelRes`),
 *   que se obtiene mediante `stringResource(...)` en la capa de interfaz.
 *
 *
 * ## Finalidad
 * Este enum permite:
 * - Centralizar la definición de opciones del perfil.
 * - Desacoplar la lógica interna del texto mostrado en pantalla.
 * - Facilitar la internacionalización (cada opción usa un `stringRes`).
 * - Simplificar el manejo de clics mediante claves limpias (`LISTS`, `RECIPES`, …).
 *
 * Se utiliza principalmente en:
 * - `Profile.kt` para construir dinámicamente la cuadrícula de opciones.
 * - `OptionsGrid.kt` para mostrar los textos correspondientes.
 *
 *
 * ## Ejemplo de uso en UI
 * ```
 * val options = ProfileOption.values()
 *
 * OptionsGrid(
 *     options = options.map { stringResource(it.labelRes) },
 *     onOptionClick = { index ->
 *         val selected = options[index]
 *         handleProfileOption(selected)
 *     }
 * )
 * ```
 *
 * @property labelRes ID del recurso de texto asociado a la opción,
 * usado para mostrar su nombre en la interfaz.
 */
enum class ProfileOption(
    @StringRes val labelRes: Int
) {
    /** Opción para ver las listas creadas por el usuario. */
    LISTS(R.string.profile_option_lists),

    /** Opción para ver las recetas creadas o guardadas. */
    RECIPES(R.string.profile_option_recipes),

    /** Ajustes generales de la aplicación. */
    SETTINGS(R.string.profile_option_settings),

    /** Estadísticas del usuario o de uso. */
    STATS(R.string.profile_option_stats),

    /** Sección de ayuda o soporte. */
    HELP(R.string.profile_option_help),

    /** Acción para iniciar sesión o cambiar de usuario. */
    LOGIN(R.string.profile_option_login)
}