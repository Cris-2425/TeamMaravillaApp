package com.example.teammaravillaapp.util

import java.util.Locale

/**
 * Utilidad para normalizar nombres y generar IDs estables (slug).
 *
 * Objetivo:
 * - Convertir un nombre humano ("Leche Entera 1L") en un identificador seguro para:
 *   - claves internas,
 *   - rutas,
 *   - IDs para listas, preferencias, etc.
 *
 * Reglas de normalización:
 * - Trim de espacios.
 * - Lowercase (con [Locale.ROOT] para evitar dependencias del idioma del dispositivo).
 * - Sustitución de caracteres con acento/ñ por equivalentes ASCII.
 * - Cualquier secuencia no alfanumérica se reemplaza por `_`.
 * - Se eliminan `_` sobrantes al inicio y al final.
 *
 * @see TAG_GLOBAL Si decides loguear normalizaciones para depurar.
 *
 * Ejemplo de uso:
 * {@code
 * val id = IdNormalizer.fromName("  Azúcar moreno  ")
 * // id == "azucar_moreno"
 * }
 */
object IdNormalizer {

    /**
     * Genera un identificador normalizado a partir de un nombre.
     *
     * @param name Nombre original (por ejemplo, introducido por el usuario).
     * Restricciones:
     * - Se recomienda que no sea blank. Si es blank, el resultado será `""`.
     *
     * @return ID normalizado (solo `a-z`, `0-9` y `_`).
     */
    fun fromName(name: String): String =
        name.trim()
            .lowercase(Locale.ROOT)
            .replace("á", "a")
            .replace("é", "e")
            .replace("í", "i")
            .replace("ó", "o")
            .replace("ú", "u")
            .replace("ü", "u")
            .replace("ñ", "n")
            .replace("[^a-z0-9]+".toRegex(), "_")
            .trim('_')
}