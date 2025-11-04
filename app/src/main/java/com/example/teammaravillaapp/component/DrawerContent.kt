package com.example.teammaravillaapp.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
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

    Spacer(Modifier.height(12.dp))
    Text(
        text = "Notificaciones",
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                // Handle click
                scope.launch { drawerState.close() }
            }
            .padding(16.dp)
    )
    Text(
        text = "Compartir lista",
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                // Handle click
                scope.launch { drawerState.close() }
            }
            .padding(16.dp)
    )
    Text(
        text = "Opciones",
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                // Handle click
                scope.launch { drawerState.close() }
            }
            .padding(16.dp)
    )
    Text(
        text = "Salir",
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                // Handle click
                scope.launch { drawerState.close() }
            }
            .padding(16.dp)
    )
}

@Preview
@Composable
fun PreviewDrawerContent() {
    DrawerContent()
}