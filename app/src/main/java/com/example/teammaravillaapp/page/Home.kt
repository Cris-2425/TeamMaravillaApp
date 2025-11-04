package com.example.teammaravillaapp.page

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.teammaravillaapp.component.Card
import com.example.teammaravillaapp.component.CardInfo
import com.example.teammaravillaapp.component.CardTitleText
// Revisar Imagen al iniciar antes de menu
@Composable
fun Inicio(cards: List<CardInfo>, modifier: Modifier, pageTitle: String) {
    CardTitleText(pageTitle)

    LazyColumn {
        cards.forEach {
            item {
                Card(it, Modifier.padding(horizontal = 0.dp, vertical = 5.dp))
            }
        }
    }
}