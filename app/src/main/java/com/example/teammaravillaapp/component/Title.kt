package com.example.teammaravillaapp.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.teammaravillaapp.R
import com.example.teammaravillaapp.ui.theme.TeamMaravillaAppTheme

/**
 * Título reutilizable. Si el texto es **"Team Maravilla"**, añade el logo.
 *
 * @param texto contenido del título.
 */
@Composable
fun Title(
    texto: String
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        if (texto == "Team Maravilla") {
            Image(
                painter = painterResource(R.drawable.logo),
                contentDescription = "Logo Team Maravilla",
                modifier = Modifier
                    .size(52.dp)
                    .padding(end = 8.dp)
            )
        }
        Text(
            text = texto,
            color = MaterialTheme.colorScheme.primary,
            fontSize = MaterialTheme.typography.titleLarge.fontSize,
            fontWeight = MaterialTheme.typography.titleLarge.fontWeight,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Preview
@Composable
fun PreviewTitle() {
    TeamMaravillaAppTheme {
        Title("Team Maravilla")
    }
}