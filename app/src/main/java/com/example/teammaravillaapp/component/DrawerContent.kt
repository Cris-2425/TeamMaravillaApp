package com.example.teammaravillaapp.component

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.teammaravillaapp.ui.theme.TeamMaravillaAppTheme
import com.example.teammaravillaapp.util.TAG_GLOBAL

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
            .background(MaterialTheme.colorScheme.background)
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
            color = MaterialTheme.colorScheme.secondary
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
            color = MaterialTheme.colorScheme.secondary
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
            color = MaterialTheme.colorScheme.secondary
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
            color = MaterialTheme.colorScheme.secondary
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
