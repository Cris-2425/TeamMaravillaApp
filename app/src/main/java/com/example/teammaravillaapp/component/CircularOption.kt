package com.example.teammaravillaapp.component

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.teammaravillaapp.ui.theme.TeamMaravillaAppTheme
import com.example.teammaravillaapp.util.TAG_GLOBAL

@Composable
fun CircularOption(
    label: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    Surface(
        onClick = {
            Log.e(TAG_GLOBAL, "BackButton click")
            onClick()
        },
        shape = CircleShape,
        color = if (selected)

            MaterialTheme.colorScheme.primary

        else

            MaterialTheme.colorScheme.secondary,

        contentColor = if (selected)

            MaterialTheme.colorScheme.onPrimary

        else

            MaterialTheme.colorScheme.onSecondary,

        modifier = Modifier
            .size(92.dp)
            .clip(CircleShape)
    ) {

        Box(
            contentAlignment = Alignment.Center
        ) {

            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (selected) {

                    Icon(Icons.Default.Check, contentDescription = "")

                    Spacer(Modifier.height(4.dp)
                    )
                }

                Text(
                    label,
                    style = MaterialTheme.typography.labelLarge,
                    textAlign = TextAlign.Center)
            }
        }
    }
}


@Preview
@Composable
fun PreviewCircularOption() {
    TeamMaravillaAppTheme {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            CircularOption(
                label = "Seleccionado",
                selected = true,
                onClick = {}
            )

            Spacer(Modifier.height(16.dp))

            CircularOption(
                label = "No seleccionado",
                selected = false,
                onClick = {}
            )
        }
    }
}
