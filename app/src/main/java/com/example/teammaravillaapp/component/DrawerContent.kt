package com.example.teammaravillaapp.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.teammaravillaapp.ui.theme.TeamMaravillaAppTheme

@Composable
fun DrawerContent(
    onNotificaciones: () -> Unit = {},
    onCompartir: () -> Unit = {},
    onOpciones: () -> Unit = {},
    onSalir: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Spacer(Modifier.height(12.dp))

        Text(
            text = "Notificaciones",
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onNotificaciones() }
                .padding(16.dp),
            color = MaterialTheme.colorScheme.secondary
        )

        Text(
            text = "Compartir lista",
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onCompartir() }
                .padding(16.dp),
            color = MaterialTheme.colorScheme.secondary
        )

        Text(
            text = "Opciones",
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onOpciones() }
                .padding(16.dp),
            color = MaterialTheme.colorScheme.secondary
        )

        Text(
            text = "Salir",
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onSalir() }
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
