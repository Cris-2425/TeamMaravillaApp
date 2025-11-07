package com.example.teammaravillaapp.component

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.teammaravillaapp.ui.theme.TeamMaravillaAppTheme
import com.example.teammaravillaapp.util.TAG_GLOBAL

@Composable
fun FilterRecipes(
    text: String,
    selected: Boolean,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = CircleShape,
        color = if (selected)

            MaterialTheme.colorScheme.secondary

        else
            MaterialTheme.colorScheme.secondaryContainer,

        contentColor = if (selected)

            MaterialTheme.colorScheme.onSecondary

        else
            MaterialTheme.colorScheme.onSecondaryContainer,
        modifier = modifier.clickable {
            Log.e(TAG_GLOBAL, "$text (selected=$selected)")
        }
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 10.dp),
            textAlign = TextAlign.Center
        )
    }
}

@Preview
@Composable
fun PreviewFilterRecipes() {
    TeamMaravillaAppTheme {
        FilterRecipes(
            text = "Recetillas",
            selected = true
        )
    }
}