package com.example.teammaravillaapp.page

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
fun Profile() {
    Box(
        Modifier.fillMaxSize()
    )
    {
        GeneralBackground()

        ProfileContent()

        Box(
            modifier = Modifier
                .align(Alignment.BottomStart)
        ) {
            BackButton()
        }
    }
}

@Composable
fun ProfileContent() {
    Column(
        Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.height(44.dp))

        ProfileImage(imageRes = null)

        Spacer(Modifier.height(30.dp))

        Text(
            "Nombre de usuario",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onBackground
        )

        Spacer(Modifier.height(64.dp))

        OptionsGrid(
            options = listOf(
                "Opción 1",
                "Opción 2",
                "Opción 3",
                "Opción 4",
                "Opción 5",
                "Opción 6"
            )
        )
    }
}

@Composable
fun ProfileImage(
    imageRes: Int? = null,
    modifier: Modifier = Modifier
) {
    if (imageRes != null) {
        Image(
            painter = painterResource(imageRes),
            contentDescription = "Foto de perfil",
            modifier = modifier
                .size(110.dp)
                .clip(RoundedCornerShape(16.dp)),
            contentScale = ContentScale.Crop
        )
    } else {
        Box(
            modifier = modifier
                .size(110.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(MaterialTheme.colorScheme.secondaryContainer),
            contentAlignment = Alignment.Center
        ) {
            Text(
                "Foto de perfil",
                color = MaterialTheme.colorScheme.onSecondaryContainer,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.labelMedium,
                modifier = Modifier.padding(8.dp)
            )
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun OptionsGrid(
    options: List<String>
) {
    FlowRow(
        maxItemsInEachRow = 3,
        horizontalArrangement = Arrangement.spacedBy(
            16.dp,
            Alignment.CenterHorizontally
        ),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        options.forEach {
            OptionBubble(it)
        }
    }
}

@Composable
fun OptionBubble(
    label: String
) {
    Surface(
        modifier = Modifier.size(90.dp),
        shape = CircleShape,
        color = MaterialTheme.colorScheme.tertiaryContainer,
        contentColor = MaterialTheme.colorScheme.onTertiaryContainer,
    ) {
        Box(
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Preview
@Composable
fun PreviewProfile() {
    TeamMaravillaAppTheme {
        Profile()
    }
}