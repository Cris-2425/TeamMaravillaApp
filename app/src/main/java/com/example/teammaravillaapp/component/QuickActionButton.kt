package com.example.teammaravillaapp.component

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.teammaravillaapp.model.QuickActionData
import com.example.teammaravillaapp.ui.theme.TeamMaravillaAppTheme
import com.example.teammaravillaapp.util.TAG_GLOBAL

/**
 * Botón compacto de **acción rápida** (icono redondo + etiqueta).
 *
 * @param quickActionButtonData datos visuales + enabled.
 * @param onClick callback al pulsar.
 */


@Composable
fun QuickActionButton(
    quickActionButtonData: QuickActionData,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    Column(
        modifier = modifier.width(72.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ElevatedCard(
            modifier = Modifier
                .size(56.dp)
                .clickable(
                    enabled = quickActionButtonData.enabled,
                    onClick = {
                        Log.e(TAG_GLOBAL, "Click en '${quickActionButtonData.label}'")
                        onClick()
                    }
                )
        ) {
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .background(MaterialTheme.colorScheme.surfaceVariant, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = quickActionButtonData.icon,
                    contentDescription = quickActionButtonData.label
                )
            }
        }
        Spacer(Modifier.height(6.dp))
        Text(
            text = quickActionButtonData.label,
            color = MaterialTheme.colorScheme.primary,
            style = MaterialTheme.typography.labelSmall,
            textAlign = TextAlign.Center,
            maxLines = 1
        )
    }
}

@Preview
@Composable
fun PreviewQuickActionButton() {
    TeamMaravillaAppTheme {
        QuickActionButton(
            QuickActionData(icon = Icons.Default.ShoppingCart, label = "Nueva lista")
        )
    }
}
