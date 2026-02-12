package com.example.teammaravillaapp.data.local.prefs.keys

import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.stringSetPreferencesKey

/**
 * Centraliza las claves utilizadas en **DataStore Preferences**.
 *
 * ### Objetivo
 * - Evitar duplicación de strings mágicos.
 * - Garantizar consistencia entre lectura y escritura.
 * - Facilitar refactorización y auditoría de persistencia.
 *
 * Las claves aquí definidas representan **estado de configuración o sesión**,
 * no datos de negocio estructurados (para eso se usa Room).
 *
 * @see androidx.datastore.preferences.core.Preferences
 */
object PrefKeys {

    /**
     * Tipo de vista de listas (ej. `grid` / `list`).
     */
    val KEY_LIST_VIEW_TYPE = stringPreferencesKey("list_view_type")

    /**
     * Modo de tema (`light`, `dark`, `system`).
     */
    val KEY_THEME_MODE = stringPreferencesKey("theme_mode")

    /**
     * Conjunto de categorías seleccionadas para filtrado.
     *
     * Persistido como `Set<String>` por simplicidad y compatibilidad con DataStore.
     */
    val KEY_CATEGORY_FILTER_SET = stringSetPreferencesKey("category_filter_set")

    /**
     * URI persistida de la foto de perfil del usuario.
     */
    val KEY_PROFILE_PHOTO = stringPreferencesKey("profile_photo_uri")

    /**
     * IDs recientes de listas, serializados como JSON (`String`).
     *
     * ### Nota de diseño
     * Se usa JSON para mantener orden y compatibilidad futura con migraciones.
     */
    val KEY_RECENT_LIST_IDS = stringPreferencesKey("recent_list_ids_json")

    /**
     * Estilo visual de lista (ej. compacto, detallado).
     */
    val KEY_LIST_STYLE = stringPreferencesKey("list_style")

    /**
     * Indica si el onboarding ha sido completado.
     *
     * Mantenerlo privado evita acceso externo accidental y refuerza
     * que su manipulación se haga vía repositorio o manager específico.
     */
    private val ONBOARDING_COMPLETED = booleanPreferencesKey("onboarding_completed")

    /**
     * Prefijo dinámico para generar claves de archivo de favoritos por usuario.
     *
     * Uso típico:
     * ```
     * val key = stringPreferencesKey(KEY_FAVORITES_FILE_ID_PREFIX + userId)
     * ```
     */
    const val KEY_FAVORITES_FILE_ID_PREFIX = "favorites_file_id_"
}