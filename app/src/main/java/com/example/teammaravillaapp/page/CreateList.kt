package com.example.teammaravillaapp.page

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.teammaravillaapp.component.ListCard

@Composable
fun HomePage(onOpenList:()->Unit, onCreateList:()->Unit){
    val listas = listOf("Compra semanal" to 12, "Cumpleaños" to 7, "Barbacoa" to 15)
    Column(modifier = Modifier.fillMaxSize().padding(12.dp)){
        Text("Mis listas")
        Spacer(Modifier.height(8.dp))
        // Button()
        Spacer(Modifier.height(12.dp))
        LazyColumn {
            items(listas){ (nombre, cantidad) ->
                ListCard(nombre, cantidad){ onOpenList() }
            }
        }
    }
}