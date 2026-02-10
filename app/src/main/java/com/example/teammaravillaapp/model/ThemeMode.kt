package com.example.teammaravillaapp.model

/**
 * Modos de tema disponibles en la aplicación.
 *
 * Este enum se utiliza para configurar la UI según la preferencia del usuario.
 *
 * Ejemplo de uso:
 * ```kotlin
 * val currentTheme = ThemeMode.DARK
 * ```
 */
enum class ThemeMode {
    /** Usa el tema del sistema (Android). */
    SYSTEM,

    /** Forzar modo claro. */
    LIGHT,

    /** Forzar modo oscuro. */
    DARK
}