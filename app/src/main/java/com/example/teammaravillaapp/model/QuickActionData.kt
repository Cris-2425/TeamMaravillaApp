package com.example.teammaravillaapp.model

import androidx.compose.ui.graphics.vector.ImageVector

/**
 * Datos para un botón de acción rápida (QuickActionButton).
 *
 * Usado en la pantalla Home para representar accesos directos como
 * “Nueva lista”, “Favoritos” o “Historial”.
 *
 * @property icon Icono que se mostrará en el botón.
 * @property label Texto del botón que describe su función.
 * @property enabled Indica si el botón está activo o deshabilitado.
 */
data class QuickActionData(
    val icon: ImageVector,
    val label: String,
    val enabled: Boolean = true
)