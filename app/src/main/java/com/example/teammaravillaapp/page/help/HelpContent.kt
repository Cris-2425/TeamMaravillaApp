package com.example.teammaravillaapp.page.help

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.teammaravillaapp.R
import com.example.teammaravillaapp.component.GeneralBackground
import com.example.teammaravillaapp.component.Title
import com.example.teammaravillaapp.page.help.component.FaqItem
import com.example.teammaravillaapp.page.help.component.HelpCard
import com.example.teammaravillaapp.page.help.component.SectionCardHelp
import com.example.teammaravillaapp.ui.theme.TeamMaravillaAppTheme

/**
 * UI pura de la pantalla de Ayuda.
 *
 * Esta función NO:
 * - accede a Context directamente para lanzar intents
 * - usa ViewModel
 * - ejecuta side-effects (solo render + callbacks)
 *
 * @param versionName Versión visible a mostrar (ej. "1.2.0"). Restricciones: no nulo.
 * @param onSendFeedback Acción de UI para enviar feedback (el contenedor lanzará el Intent). Restricciones: no nulo.
 * @param onBack Acción para volver (si decides pintar un botón “Volver”). Restricciones: no nulo.
 *
 * @see HelpScreen Contenedor que implementa la lógica de plataforma.
 *
 * Ejemplo de uso:
 * {@code
 * HelpContent(
 *   versionName = "1.0.0",
 *   onSendFeedback = { /* launch intent */ },
 *   onBack = { navController.popBackStack() }
 * )
 * }
 */
@Composable
fun HelpContent(
    versionName: String,
    onSendFeedback: () -> Unit,
    onBack: () -> Unit
) {
    GeneralBackground(overlayAlpha = 0.20f) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 14.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Title(texto = stringResource(R.string.help_title))

            Text(
                text = stringResource(R.string.help_subtitle),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            HelpCard(
                title = stringResource(R.string.help_quick_start_title),
                body = stringResource(R.string.help_quick_start_body)
            )

            SectionCardHelp(title = stringResource(R.string.help_faq_title)) {
                FaqItem(
                    question = stringResource(R.string.help_faq_q1),
                    answer = stringResource(R.string.help_faq_a1)
                )
                FaqItem(
                    question = stringResource(R.string.help_faq_q2),
                    answer = stringResource(R.string.help_faq_a2)
                )
                FaqItem(
                    question = stringResource(R.string.help_faq_q3),
                    answer = stringResource(R.string.help_faq_a3)
                )
                FaqItem(
                    question = stringResource(R.string.help_faq_q4),
                    answer = stringResource(R.string.help_faq_a4)
                )
            }

            SectionCardHelp(title = stringResource(R.string.help_contact_title)) {
                Text(
                    text = stringResource(R.string.help_contact_body),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(Modifier.height(10.dp))

                Button(
                    onClick = onSendFeedback,
                    modifier = Modifier.fillMaxWidth(),
                    contentPadding = PaddingValues(vertical = 14.dp)
                ) {
                    Text(stringResource(R.string.help_contact_button))
                }

                Spacer(Modifier.height(6.dp))
                Text(
                    text = stringResource(R.string.help_version_format, versionName),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            }
            //Spacer(Modifier.height(70.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewHelpContent() {
    TeamMaravillaAppTheme {
        HelpContent(
            versionName = "1.2.0",
            onSendFeedback = {},
            onBack = {}
        )
    }
}