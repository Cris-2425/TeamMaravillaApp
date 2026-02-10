package com.example.teammaravillaapp.page.help.component

import android.content.Context
import android.content.Intent
import androidx.core.net.toUri
import com.example.teammaravillaapp.R


/**
 * Construye un [Intent] ACTION_SENDTO para enviar feedback por email con asunto/cuerpo pre-rellenados.
 *
 * Se separa para:
 * - mejorar legibilidad del contenedor
 * - permitir tests unitarios “puros” (sin Compose)
 *
 * @param context Contexto para resolver strings localizadas (i18n). Restricciones: no nulo.
 * @param versionName Versión legible de la app (ej. "1.2.0"). Restricciones: no nulo / no blank recomendado.
 * @param versionCode Código interno de versión. Restricciones: >= 1 recomendado.
 *
 * @return Intent listo para lanzarse con chooser.
 *
 * @see Intent.ACTION_SENDTO
 *
 * Ejemplo de uso:
 * {@code
 * val intent = buildFeedbackEmailIntent(context, "1.0.0", 1)
 * context.startActivity(Intent.createChooser(intent, "Enviar con..."))
 * }
 */
internal fun buildFeedbackEmailIntent(
    context: Context,
    versionName: String,
    versionCode: Int
): Intent {
    val subject = context.getString(R.string.help_feedback_subject, versionName)
    val body = context.getString(R.string.help_feedback_body, versionName, versionCode)

    return Intent(Intent.ACTION_SENDTO).apply {
        data = "mailto:".toUri()
        putExtra(Intent.EXTRA_EMAIL, arrayOf("teammaravillaapp@gmail.com"))
        putExtra(Intent.EXTRA_SUBJECT, subject)
        putExtra(Intent.EXTRA_TEXT, body)
    }
}