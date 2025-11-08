package com.example.teammaravillaapp.component

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.teammaravillaapp.ui.theme.TeamMaravillaAppTheme
import com.example.teammaravillaapp.util.TAG_GLOBAL

/**
 * Burbuja circular de opción simple (perfil).
 *
 * Solo pinta y loguea; añade navegación fuera.
 */
@Composable
fun OptionBubble(
    label: String
) {
    Surface(
        modifier = Modifier
            .size(90.dp)
            .clickable {
                Log.e(TAG_GLOBAL, "Profile → Click en opción: $label")
                       },
        shape = CircleShape,
        color = MaterialTheme.colorScheme.tertiaryContainer,
        contentColor = MaterialTheme.colorScheme.onTertiaryContainer,
    ) {
        Box(contentAlignment = Alignment.Center) {
            Text(
                text = label,
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Preview
@Composable
fun PreviewOptionBubble() {
    TeamMaravillaAppTheme {
        OptionBubble("Mis Listas")
    }
}