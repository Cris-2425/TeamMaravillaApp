package com.example.teammaravillaapp.model

/**
 * Pestañas o botones principales de la barra inferior de navegación.
 *
 * Cada valor representa una sección principal de la app:
 * - HOME: Pantalla principal
 * - RECIPES: Sección de recetas
 * - HISTORY: Historial de listas o compras
 * - PROFILE: Perfil del usuario
 *
 * Nota:
 *  - La convención de nombres en enums suele ser PascalCase, pero se mantiene el
 *    identificador actual en mayúsculas para no romper código existente.
 *
 * Ejemplo de uso:
 * ```kotlin
 * when(button) {
 *     OptionButton.HOME -> navigateToHome()
 *     OptionButton.PROFILE -> navigateToProfile()
 * }
 * ```
 */
enum class OptionButton {
    HOME,
    RECIPES,
    HISTORY,
    PROFILE
}