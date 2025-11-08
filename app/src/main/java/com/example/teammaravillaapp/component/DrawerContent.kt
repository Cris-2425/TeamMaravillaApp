package com.example.teammaravillaapp.component

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.teammaravillaapp.ui.theme.TeamMaravillaAppTheme
import com.example.teammaravillaapp.util.TAG_GLOBAL

/**
 * Contenido del **drawer lateral**.
 *
 * Solo resuelve callbacks (notificaciones, compartir, opciones, salir).
 */
@Composable
fun DrawerContent(
    onNotifications: () -> Unit = {},
    onShare: () -> Unit = {},
    onOptions: () -> Unit = {},
    onExit: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.secondary)
            .padding(vertical = 8.dp)
    ) {
        Spacer(Modifier.height(12.dp))

        Text(
            text = "Notificaciones",
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    Log.e(TAG_GLOBAL, "Notificaciones")
                    onNotifications()
                }
                .padding(16.dp),
            color = MaterialTheme.colorScheme.onSecondary
        )

        Text(
            text = "Compartir lista",
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    Log.e(TAG_GLOBAL, "Compartir lista")
                    onShare()
                }
                .padding(16.dp),
            color = MaterialTheme.colorScheme.onSecondary
        )

        Text(
            text = "Opciones",
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    Log.e(TAG_GLOBAL, "Opciones")
                    onOptions()
                }
                .padding(16.dp),
            color = MaterialTheme.colorScheme.onSecondary
        )

        Text(
            text = "Salir",
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    Log.e(TAG_GLOBAL, "Salir")
                    onExit()
                }
                .padding(16.dp),
            color = MaterialTheme.colorScheme.onSecondary
        )
    }
}

@Preview
@Composable
fun PreviewDrawerContent() {
    TeamMaravillaAppTheme {
        DrawerContent()
    }
}