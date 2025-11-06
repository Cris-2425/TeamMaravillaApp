package com.example.teammaravillaapp.page

import ProductBubble
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.teammaravillaapp.component.BackButton
import com.example.teammaravillaapp.component.GeneralBackground
import com.example.teammaravillaapp.ui.theme.TeamMaravillaAppTheme
import androidx.compose.ui.tooling.preview.Preview
import com.example.teammaravillaapp.model.Product
import com.example.teammaravillaapp.component.Title

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ListDetail() {
    Box(Modifier
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

            Row(Modifier
                .fillMaxWidth(),
                horizontalArrangement = Arrangement.Start) {

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

            Text("Productos en la lista: ",
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
                    listOf(
                        "Leche",
                        "Huevos",
                        "Pan",
                        "Arroz",
                        "Café"
                    ).forEach {
                        ProductBubble(Product(it))
                    }
                }
            }

            Text("Usados recientemente",
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
                    listOf(
                        "Harina",
                        "Yogur",
                        "Atún",
                        "Queso"
                    ).forEach {
                        ProductBubble(Product(it))
                    }
                }
            }

            Text("Frutas y vegetales",
                style = MaterialTheme.typography.titleSmall
            )

            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                listOf(
                    "Manzana",
                    "Zanahoria",
                    "Lechuga",
                    "Tomate"
                ).forEach {
                    ProductBubble(Product(it))
                }
            }

            Text("Carne y pescado",
                style = MaterialTheme.typography.titleSmall
            )

            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                listOf(
                    "Pollo",
                    "Ternera",
                    "Atún",
                    "Salmón")
                    .forEach {
                        ProductBubble(Product(it))
                    }
            }

            Text("Otros",
                style = MaterialTheme.typography.titleSmall
            )

            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                listOf(
                    "Aceite",
                    "Sal",
                    "Papel",
                    "Detergente"
                ).forEach {
                    ProductBubble(Product(it))
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
fun PreviewListDetail() {
    TeamMaravillaAppTheme {
        ListDetail()
    }
}