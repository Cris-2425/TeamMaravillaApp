package com.example.teammaravillaapp.page.listdetail.component

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.teammaravillaapp.R
import com.example.teammaravillaapp.ui.theme.TeamMaravillaAppTheme

/**
 * Título de la sección de productos actuales de la lista.
 *
 * @param modifier Modificador de Compose para controlar layout.
 */
@Composable
internal fun CurrentProductsTitle(
    modifier: Modifier = Modifier
) {
    Text(
        modifier = modifier,
        text = stringResource(R.string.list_detail_current_products_title),
        style = MaterialTheme.typography.titleSmall
    )
}

@Preview(showBackground = true)
@Composable
private fun CurrentProductsTitlePreview() {
    TeamMaravillaAppTheme {
        CurrentProductsTitle()
    }
}