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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.teammaravillaapp.R
import com.example.teammaravillaapp.ui.theme.TeamMaravillaAppTheme

/**
 * Título reutilizable con opción de mostrar el logo de la app.
 *
 * @param texto Texto del título a mostrar.
 * @param modifier Modificador de Compose para controlar layout.
 * @param showLogo Indica si se debe mostrar el logo a la izquierda del texto.
 *
 * Ejemplo de uso:
 * {@code
 * Title(
 *   texto = stringResource(R.string.app_title),
 *   showLogo = true
 * )
 * }
 */
@Composable
fun Title(
    texto: String,
    modifier: Modifier = Modifier,
    showLogo: Boolean = false
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (showLogo) {
            Image(
                painter = painterResource(R.drawable.logo),
                contentDescription = stringResource(R.string.logo_content_description),
                modifier = Modifier
                    .size(52.dp)
                    .padding(end = 8.dp)
            )
        }

        Text(
            text = texto,
            color = MaterialTheme.colorScheme.primary,
            fontSize = MaterialTheme.typography.titleLarge.fontSize,
            fontWeight = MaterialTheme.typography.titleLarge.fontWeight ?: FontWeight.SemiBold,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun TitlePreview() {
    TeamMaravillaAppTheme {
        Title(
            texto = stringResource(R.string.app_title),
            showLogo = true
        )
    }
}