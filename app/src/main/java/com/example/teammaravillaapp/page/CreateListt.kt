package com.example.teammaravillaapp.page

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.teammaravillaapp.R
import com.example.teammaravillaapp.component.BackButton
import com.example.teammaravillaapp.component.GeneralBackground
import com.example.teammaravillaapp.component.Title
import com.example.teammaravillaapp.ui.theme.TeamMaravillaAppTheme

@Composable
fun CreateListt() {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {

        GeneralBackground()

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
    Column(Modifier.fillMaxSize()) {

        CreateListTopBar()

        Column(
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            Spacer(Modifier.height(12.dp))

            OutlinedTextField(
                value = "",
                onValueChange = {},
                placeholder = {
                    Text("Nombre lista")
                },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(16.dp))

            Text("Fondo de la lista",
                style = MaterialTheme.typography.titleMedium)

            Spacer(Modifier.height(8.dp))

            BackgroundGrid()

            Spacer(Modifier.height(16.dp))

            Text("Listas sugeridas",
                style = MaterialTheme.typography.titleMedium)

            Spacer(Modifier.height(8.dp))

            SuggestedListsSection(
                items = listOf(
                    "Compra semanal",
                    "BBQ sábado",
                    "Desayunos",
                    "Limpieza")
            )

            Spacer(Modifier.height(24.dp))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateListTopBar() {
    CenterAlignedTopAppBar(
        title = {
            Title("Crear Lista")
        },
        navigationIcon = {
            Button(
                onClick = {},
                enabled = false
            ) {
                Text("Cancelar")
            }
        },
        actions = {
            Button(
                onClick = {},
                enabled = false
            ) {
                Text("Guardar")
            }
        }
    )
}

@Composable
fun BackgroundGrid() {

    Row(
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier.fillMaxWidth())
    {

        Column(
            Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            BackgroundTile(selected = true, label = "Fondo1")

            BackgroundTile(label = "Fondo3")
        }

        Column(
            Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            BackgroundTile(label = "Fondo2")

            BackgroundTile(label = "Fondo4")
        }
    }
}

@Composable
fun BackgroundTile(
    selected: Boolean = false,
    label: String
) {
    val imageRes = when (label) {
        "Fondo1" -> R.drawable.fondo_farmacia
        "Fondo2" -> R.drawable.fondo_bbq
        "Fondo3" -> R.drawable.fondo_desayuno
        "Fondo4" -> R.drawable.fondo_limpieza
        else -> null
    }
    Box(
        modifier = Modifier
            .height(76.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(MaterialTheme.colorScheme.background)
            .border(
                width = if (selected) 2.dp else 1.dp,
                color = if (selected)
                    MaterialTheme.colorScheme.primary
                else
                    MaterialTheme.colorScheme.secondary,
                shape = RoundedCornerShape(12.dp)
            ),
        contentAlignment = Alignment.Center

    ) {
        if (imageRes != null) {
            Image(
                painter = painterResource(id = imageRes),
                contentDescription = "",
                modifier = Modifier.matchParentSize(),
                contentScale = ContentScale.Crop
            )
        }
        if (selected) {
            Icon(
                Icons.Default.Check,
                contentDescription = "",
                tint = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(8.dp)
                    .size(22.dp)
                    .background(MaterialTheme.colorScheme.primary, CircleShape)
                    .padding(3.dp)
            )
        }
        Text(label, color = MaterialTheme.colorScheme.onBackground)
    }
}

@Composable
fun SuggestedListsSection(
    items: List<String>
) {
    // No me dejaba scrollear con LazyRow
    FlowRow(
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items.forEach {
            SuggestedListBubble(it)
        }
    }
}

@Composable
fun SuggestedListBubble(
    name: String
) {

    Surface(
        modifier = Modifier.size(72.dp),
        shape = CircleShape,
        color = MaterialTheme.colorScheme.secondary
    ) {

        Box(
            contentAlignment = Alignment.Center
        ) {

            Text(
                text = name,
                color = MaterialTheme.colorScheme.onSecondary,
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center,
            )
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