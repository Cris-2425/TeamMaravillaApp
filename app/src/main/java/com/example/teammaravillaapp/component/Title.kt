package com.example.teammaravillaapp.component

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun Title(texto: String) {

    Text(text = texto,
        modifier = Modifier,
        color = MaterialTheme.colorScheme.primary,
        fontSize = MaterialTheme.typography.titleLarge.fontSize,
        fontWeight = MaterialTheme.typography.titleLarge.fontWeight,
        maxLines = 2,
        overflow = TextOverflow.Ellipsis // Pone los 3 puntitos si no entra.
    )
}

@Preview
@Composable
fun PreviewTitle() {
    Title ("Lista de la Compra")
}