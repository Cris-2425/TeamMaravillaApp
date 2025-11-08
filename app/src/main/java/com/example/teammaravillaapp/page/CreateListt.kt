package com.example.teammaravillaapp.page

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.teammaravillaapp.component.BackButton
import com.example.teammaravillaapp.component.BackgroundGrid
import com.example.teammaravillaapp.component.CreateListTopBar
import com.example.teammaravillaapp.component.GeneralBackground
import com.example.teammaravillaapp.component.ListBackgrounds
import com.example.teammaravillaapp.component.SuggestedListSection
import com.example.teammaravillaapp.model.SuggestedListData
import com.example.teammaravillaapp.ui.theme.TeamMaravillaAppTheme
import com.example.teammaravillaapp.util.TAG_GLOBAL

@Composable
fun CreateListt() {
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {

        CreateListContent()

        Box(
            modifier = Modifier
                .align(Alignment.BottomStart)
        ) {

            BackButton()
        }
    }
}

@Composable
fun CreateListContent() {
    var name by rememberSaveable { mutableStateOf("") }
    var selectedBackground by rememberSaveable { mutableStateOf(ListBackgrounds.Fondo1) }
    val backgroundRes = ListBackgrounds.getBackgroundRes(selectedBackground)

    Box(
        Modifier
            .fillMaxSize()
    ) {
        GeneralBackground(backgroundRes)

        Column(
            Modifier
                .fillMaxSize()
        ) {

            CreateListTopBar()

            Column(
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {

                Spacer(Modifier.height(12.dp))

                OutlinedTextField(
                    value = name,
                    onValueChange = {
                        name = it
                        Log.e(TAG_GLOBAL, "Nombre lista cambió a: '$it'")
                    },
                    placeholder = {
                        Text("Nombre lista")
                                  },
                    modifier = Modifier
                        .fillMaxWidth(),
                    singleLine = true
                )

                Spacer(Modifier.height(16.dp))

                Text(
                    "Fondo de la lista",
                    style = MaterialTheme.typography.titleMedium
                )

                Spacer(Modifier.height(8.dp))

                BackgroundGrid(
                    selectedLabel = selectedBackground,
                    onSelect = {
                        chosen -> selectedBackground = chosen
                    }
                )

                Spacer(Modifier.height(16.dp))

                Text(
                    "Listas sugeridas",
                    style = MaterialTheme.typography.titleMedium
                )

                Spacer(Modifier.height(8.dp))

                SuggestedListSection(items = SuggestedListData.items) {
                    name = it.name
                    Log.e(TAG_GLOBAL, "Lista sugerida pulsada: '${it.name}'")
                }

                Spacer(Modifier.height(24.dp))
            }
        }

        Box(
            Modifier
                .align(Alignment.BottomStart)
        ) {
            BackButton()
        }
    }
}

@Preview
@Composable
fun PreviewCreateListt() {
    TeamMaravillaAppTheme {
        CreateListt()
    }
}