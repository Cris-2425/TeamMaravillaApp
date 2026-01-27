package com.example.teammaravillaapp.page.help

import android.content.Intent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.teammaravillaapp.BuildConfig
import com.example.teammaravillaapp.R
import com.example.teammaravillaapp.component.BackButton
import com.example.teammaravillaapp.component.GeneralBackground
import com.example.teammaravillaapp.component.Title
import androidx.core.net.toUri

@Composable
fun Help(
    onBack: () -> Unit
) {
    val ctx = LocalContext.current

    fun sendFeedbackEmail() {
        val intent = Intent(Intent.ACTION_SENDTO).apply {
            data = "mailto:".toUri() // obligatorio para apps de email
            putExtra(Intent.EXTRA_EMAIL, arrayOf("teammaravillaapp@gmail.com"))
            putExtra(Intent.EXTRA_SUBJECT, "Feedback TeamMaravillaApp (${BuildConfig.VERSION_NAME})")
            putExtra(
                Intent.EXTRA_TEXT,
                "Hola!\n\nQuería comentar lo siguiente:\n\n\n---\nVersión: ${BuildConfig.VERSION_NAME} (${BuildConfig.VERSION_CODE})\n"
            )
        }
        ctx.startActivity(Intent.createChooser(intent, "Enviar feedback"))
    }

    Box(Modifier.fillMaxSize()) {
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
                        q = stringResource(R.string.help_faq_q1),
                        a = stringResource(R.string.help_faq_a1)
                    )
                    FaqItem(
                        q = stringResource(R.string.help_faq_q2),
                        a = stringResource(R.string.help_faq_a2)
                    )
                    FaqItem(
                        q = stringResource(R.string.help_faq_q3),
                        a = stringResource(R.string.help_faq_a3)
                    )
                    FaqItem(
                        q = stringResource(R.string.help_faq_q4),
                        a = stringResource(R.string.help_faq_a4)
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
                        onClick = ::sendFeedbackEmail,
                        modifier = Modifier.fillMaxWidth(),
                        contentPadding = PaddingValues(vertical = 14.dp)
                    ) {
                        Text(stringResource(R.string.help_contact_button))
                    }

                    Spacer(Modifier.height(6.dp))
                    Text(
                        text = "v${BuildConfig.VERSION_NAME}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                }

                Spacer(Modifier.height(70.dp))
            }
        }

        Box(Modifier.align(Alignment.BottomStart)) {
            BackButton(onClick = onBack)
        }
    }
}

@Composable
private fun HelpCard(title: String, body: String) {
    Surface(
        shape = MaterialTheme.shapes.extraLarge,
        tonalElevation = 2.dp,
        color = MaterialTheme.colorScheme.surface
    ) {
        Column(Modifier.fillMaxWidth().padding(14.dp)) {
            Text(title, style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(6.dp))
            Text(body, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}

@Composable
private fun SectionCardHelp(title: String, content: @Composable ColumnScope.() -> Unit) {
    Surface(
        shape = MaterialTheme.shapes.extraLarge,
        tonalElevation = 2.dp,
        color = MaterialTheme.colorScheme.surface
    ) {
        Column(
            Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            content = {
                Text(title, style = MaterialTheme.typography.titleMedium)
                Divider()
                content()
            }
        )
    }
}

@Composable
private fun FaqItem(q: String, a: String) {
    var expanded = androidx.compose.runtime.remember { androidx.compose.runtime.mutableStateOf(false) }
    Surface(
        shape = MaterialTheme.shapes.large,
        color = MaterialTheme.colorScheme.surfaceContainerLow
    ) {
        Column(
            Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            TextButton(onClick = { expanded.value = !expanded.value }, contentPadding = PaddingValues(0.dp)) {
                Text(q, style = MaterialTheme.typography.titleSmall)
            }
            if (expanded.value) {
                Spacer(Modifier.height(6.dp))
                Text(a, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
    }
}
