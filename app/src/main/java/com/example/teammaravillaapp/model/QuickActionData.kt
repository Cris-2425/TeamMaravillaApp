package com.example.teammaravillaapp.model

import androidx.compose.ui.graphics.vector.ImageVector

/**
 * Datos para un botón de acción rápida (**QuickActionButton**).
 *
 * Este modelo se utiliza en la **pantalla Home** para representar accesos directos,
 * como “Nueva lista”, “Favoritos” o “Historial”.
 *
 * Ejemplo de uso:
 * ```kotlin
 * val quickAction = QuickActionData(
 *     icon = Icons.Default.Add,
 *     label = "Nueva lista"
 * )
 * ```
 *
 * @property icon Icono vectorial que se mostrará en el botón (Compose `ImageVector`).
 * @property label Texto descriptivo de la acción del botón.
 * @property enabled Indica si el botón está activo o deshabilitado. Por defecto es `true`.
 */
data class QuickActionData(
    val icon: ImageVector,
    val label: String,
    val enabled: Boolean = true
)