package com.example.teammaravillaapp.page.listdetail.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.teammaravillaapp.R
import com.example.teammaravillaapp.ui.theme.TeamMaravillaAppTheme

/**
 * Tarjeta informativa que se muestra cuando la lista aún no tiene productos.
 *
 * @param modifier Modificador de Compose para controlar layout.
 */
@Composable
internal fun EmptyCurrentProductsCard(
    modifier: Modifier = Modifier
) {
    Surface(
        shape = MaterialTheme.shapes.medium,
        color = MaterialTheme.colorScheme.secondaryContainer,
        contentColor = MaterialTheme.colorScheme.onSecondaryContainer,
        modifier = modifier
    ) {
        Box(
            Modifier
                .fillMaxWidth()
                .heightIn(min = 80.dp)
                .padding(12.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = stringResource(R.string.list_detail_no_products),
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun EmptyCurrentProductsCardPreview() {
    TeamMaravillaAppTheme {
        EmptyCurrentProductsCard(modifier = Modifier.padding(16.dp))
    }
}