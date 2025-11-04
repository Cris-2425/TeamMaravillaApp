package com.example.teammaravillaapp.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

@Composable
fun DrawerContent() {
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()

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
                .clickable {
                    scope.launch { drawerState.close() }
                }
                .padding(16.dp),
            color = MaterialTheme.colorScheme.primary,
        )

        Text(
            text = "Compartir lista",
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    scope.launch { drawerState.close() }
                }
                .padding(16.dp),
            color = MaterialTheme.colorScheme.primary,
        )

        Text(
            text = "Opciones",
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    scope.launch { drawerState.close() }
                }
                .padding(16.dp),
            color = MaterialTheme.colorScheme.primary,
        )

        Text(
            text = "Salir",
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    scope.launch { drawerState.close() }
                }
                .padding(16.dp),
            color = MaterialTheme.colorScheme.primary,
        )
    }
}

@Preview
@Composable
fun PreviewDrawerContent() {
    DrawerContent()
}
