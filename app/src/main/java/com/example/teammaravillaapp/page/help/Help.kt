package com.example.teammaravillaapp.page.help

import android.content.ActivityNotFoundException
import android.content.Intent
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.example.teammaravillaapp.BuildConfig
import com.example.teammaravillaapp.R
import com.example.teammaravillaapp.page.help.component.buildFeedbackEmailIntent
import com.example.teammaravillaapp.ui.events.UiEvent
import com.example.teammaravillaapp.ui.theme.TeamMaravillaAppTheme

/**
 * Pantalla contenedora de Ayuda.
 *
 * Responsabilidades:
 * - Resolver dependencias Android (Context, Intent, BuildConfig).
 * - Construir y lanzar el intent de envío de feedback por email.
 * - Traducir errores de plataforma (sin app de email) a eventos de UI vía [onUiEvent].
 * - Delegar la UI a [HelpContent] (presentación pura).
 *
 * Motivo de diseño:
 * - Esta pantalla no tiene “estado de negocio” ni datos que observar; no compensa un ViewModel.
 * - El único efecto lateral es lanzar un Intent, que es responsabilidad típica del contenedor.
 *
 * @param onBack Callback para navegación hacia atrás. Restricciones: no nulo.
 * @param onUiEvent Consumidor de eventos one-shot (snackbars). Restricciones: no nulo.
 *
 * @see HelpContent UI pura.
 * @see buildFeedbackEmailIntent Construcción del Intent (testable y reutilizable).
 *
 * Ejemplo de uso:
 * {@code
 * HelpScreen(
 *   onBack = navController::popBackStack,
 *   onUiEvent = { event -> handleUiEvent(event) }
 * )
 * }
 */
@Composable
fun Help(
    onBack: () -> Unit,
    onUiEvent: (UiEvent) -> Unit
) {
    val context = LocalContext.current

    fun sendFeedbackEmail() {
        val intent = buildFeedbackEmailIntent(
            context = context,
            versionName = BuildConfig.VERSION_NAME,
            versionCode = BuildConfig.VERSION_CODE
        )

        try {
            context.startActivity(
                Intent.createChooser(
                    intent,
                    context.getString(R.string.help_send_feedback_chooser)
                )
            )
        } catch (_: ActivityNotFoundException) {
            onUiEvent(UiEvent.ShowSnackbar(R.string.snackbar_email_no_app))
        } catch (_: SecurityException) {
            onUiEvent(UiEvent.ShowSnackbar(R.string.snackbar_action_failed))
        }
    }

    HelpContent(
        versionName = BuildConfig.VERSION_NAME,
        onSendFeedback = ::sendFeedbackEmail,
        onBack = onBack
    )
}

@Preview(showBackground = true)
@Composable
private fun PreviewHelpScreen() {
    TeamMaravillaAppTheme {
        Help(onBack = {}, onUiEvent = {})
    }
}