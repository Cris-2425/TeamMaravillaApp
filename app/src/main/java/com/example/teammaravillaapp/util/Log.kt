package com.example.teammaravillaapp.util

/**
 * Etiqueta global de logs de la aplicación.
 *
 * Se utiliza como `tag` en todas las llamadas a `Log.*` para:
 * - filtrar fácilmente mensajes en Logcat,
 * - mantener consistencia en todo el proyecto,
 * - evitar etiquetas hardcodeadas y errores tipográficos.
 *
 * Ejemplo:
 * {@code
 * Log.d(TAG_GLOBAL, "Pantalla Home abierta")
 * }
 */
const val TAG_GLOBAL: String = "TeamMaravillaApp"