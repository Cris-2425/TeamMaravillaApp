package com.example.teammaravillaapp

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

/**
 * Clase [Application] de TeamMaravillaApp.
 *
 * Responsabilidades:
 * - Activar el container de inyección de dependencias de Hilt a nivel aplicación.
 *
 * Nota:
 * - No contiene lógica de negocio. Cualquier inicialización global (si la hubiera) debería
 *   ser ligera y preferiblemente delegada a módulos/servicios específicos.
 */
@HiltAndroidApp
class TeamMaravillaApp : Application()