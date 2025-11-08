package com.example.teammaravillaapp.page

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.teammaravillaapp.component.BackButton
import com.example.teammaravillaapp.component.GeneralBackground
import com.example.teammaravillaapp.component.ProductBubble
import com.example.teammaravillaapp.component.Title
import com.example.teammaravillaapp.model.*
import com.example.teammaravillaapp.ui.theme.TeamMaravillaAppTheme

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ListDetail() {

    val productosLista = listOf(Leche, Huevo, Pan, Arroz, Cafe)
    val usadosRecientemente = listOf(Harina, Yogur, Atun, Queso)
    val frutasYVerduras = listOf(Manzana, Zanahoria, Lechuga, Tomate)
    val carneYPescado = listOf(Pollo, Ternera, Atun, Salmon)
    val otros = listOf(Aceite, Azucar, PapelCocina, Detergente)


    Box(
        Modifier
        .fillMaxSize()
    ) {

        GeneralBackground()

        Column(
            Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            Title("Nombre lista")

            Row(
                Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Start
            ) {
                Surface(
                    shape = MaterialTheme.shapes.large,
                    color = MaterialTheme.colorScheme.secondary,
                    contentColor = MaterialTheme.colorScheme.onSecondary
                ) {
                    Text(
                        "Ordenar lista",
                        style = MaterialTheme.typography.labelLarge,
                        modifier = Modifier
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                    )
                }
            }

            Text(
                "Productos en la lista:",
                style = MaterialTheme.typography.titleSmall)

            Surface(
                shape = MaterialTheme.shapes.medium,
                color = MaterialTheme.colorScheme.error,
                contentColor = MaterialTheme.colorScheme.onError,
            ) {
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp)
                ) {
                    productosLista.forEach {
                        ProductBubble(it)
                    }
                }
            }

            Text(
                "Usados recientemente",
                style = MaterialTheme.typography.titleSmall)

            Surface(
                shape = MaterialTheme.shapes.medium,
                color = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
            ) {
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp)
                ) {
                    usadosRecientemente.forEach {
                        ProductBubble(it)
                    }
                }
            }

            Text("Frutas y vegetales",
                style = MaterialTheme.typography.titleSmall)

            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                frutasYVerduras.forEach {
                    ProductBubble(it)
                }
            }

            Text(
                "Carne y pescado",
                style = MaterialTheme.typography.titleSmall)

            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            )
            {
                carneYPescado.forEach {
                    ProductBubble(it)
                }
            }

            Text(
                "Otros",
                style = MaterialTheme.typography.titleSmall)

            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                otros.forEach {
                    ProductBubble(it)
                }
            }

            Spacer(Modifier.weight(1f))

            Box(
                Modifier
                    .fillMaxWidth()
                    .height(48.dp)
            ) {

                Text(
                    "Buscador dinámico",
                    modifier = Modifier.align(Alignment.Center),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }

        Box(Modifier
            .align(Alignment.BottomStart)
        ) {
            BackButton()
        }
    }
}

@Preview
@Composable
fun PreviewListDetail() {
    TeamMaravillaAppTheme {
        ListDetail()
    }
}