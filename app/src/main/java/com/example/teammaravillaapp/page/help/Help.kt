package com.example.teammaravillaapp.page.help

import android.content.ActivityNotFoundException
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
import androidx.core.net.toUri
import com.example.teammaravillaapp.BuildConfig
import com.example.teammaravillaapp.R
import com.example.teammaravillaapp.component.GeneralBackground
import com.example.teammaravillaapp.component.Title
import com.example.teammaravillaapp.ui.events.UiEvent

@Composable
fun Help(
    onBack: () -> Unit,
    onUiEvent: (UiEvent) -> Unit = {}
) {
    val ctx = LocalContext.current

    fun sendFeedbackEmail() {
        val subject = ctx.getString(
            R.string.help_feedback_subject,
            BuildConfig.VERSION_NAME
        )

        val body = ctx.getString(
            R.string.help_feedback_body,
            BuildConfig.VERSION_NAME,
            BuildConfig.VERSION_CODE
        )

        val intent = Intent(Intent.ACTION_SENDTO).apply {
            data = "mailto:".toUri()
            putExtra(Intent.EXTRA_EMAIL, arrayOf("teammaravillaapp@gmail.com"))
            putExtra(Intent.EXTRA_SUBJECT, subject)
            putExtra(Intent.EXTRA_TEXT, body)
        }

        try {
            ctx.startActivity(
                Intent.createChooser(
                    intent,
                    ctx.getString(R.string.help_send_feedback_chooser)
                )
            )
        } catch (_: ActivityNotFoundException) {
            onUiEvent(UiEvent.ShowSnackbar(R.string.snackbar_email_no_app))
        }
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

        //Box(Modifier.align(Alignment.BottomStart)) {
        //    BackButton(onClick = onBack)
        //}
    }
}