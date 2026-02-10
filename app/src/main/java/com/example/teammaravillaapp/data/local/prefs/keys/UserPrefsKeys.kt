package com.example.teammaravillaapp.data.local.prefs.keys

import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.stringSetPreferencesKey

/**
 * Contiene las claves utilizadas en DataStore Preferences.
 *
 * Cada clave corresponde a un valor persistido de configuración o estado de usuario.
 */
object PrefKeys {

    /** Tipo de vista de las listas de usuario (Grid/List). */
    val KEY_LIST_VIEW_TYPE = stringPreferencesKey("list_view_type")

    /** Tema de la aplicación (Light/Dark/System). */
    val KEY_THEME_MODE = stringPreferencesKey("theme_mode")

    /** Conjunto de categorías seleccionadas para filtrar productos. */
    val KEY_CATEGORY_FILTER_SET = stringSetPreferencesKey("category_filter_set")

    /** URI de la foto de perfil del usuario. */
    val KEY_PROFILE_PHOTO = stringPreferencesKey("profile_photo_uri")

    /** IDs recientes de listas de usuario (JSON array string). */
    val KEY_RECENT_LIST_IDS = stringPreferencesKey("recent_list_ids_json")

    /** Estilo de lista (opcional: compacto, detallado, etc.). */
    val KEY_LIST_STYLE = stringPreferencesKey("list_style")

    /** Indica si el onboarding inicial ha sido completado. */
    private val ONBOARDING_COMPLETED = booleanPreferencesKey("onboarding_completed")
}