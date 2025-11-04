package com.example.teammaravillaapp.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.teammaravillaapp.R

class CardInfo(
    var imageID: Int,
    var imageDescription: String = "",
    var title: String,
    var subtitle: String
)
@Composable
fun Card(cardInfo: CardInfo, modifier: Modifier = Modifier) {
    Card(modifier = Modifier.size(width = 240.dp, 100.dp)) {
        Row(modifier = Modifier) {
            Image (
                painterResource(id = cardInfo.imageID),
                contentDescription = cardInfo.imageDescription,
                modifier = Modifier.padding(5.dp)
            )
            Column {
                CardTitleText(cardInfo.title)
                CardText(cardInfo.subtitle)
            }
        }
    }
}

@Composable
fun CardTitleText(title: String) {
    Text(text = title,
        modifier = Modifier,
        color = MaterialTheme.colorScheme.secondary,
        fontSize = MaterialTheme.typography.titleLarge.fontSize,
        fontWeight = MaterialTheme.typography.titleLarge.fontWeight,
        maxLines = 2,
        overflow = TextOverflow.Ellipsis
    )
}

@Composable
fun CardText(subtitle: String) {
    Text(text = subtitle,
        modifier = Modifier,
        color = MaterialTheme.colorScheme.tertiary,
        fontSize = MaterialTheme.typography.titleLarge.fontSize,
        fontWeight = MaterialTheme.typography.titleLarge.fontWeight,
        maxLines = 2,
        overflow = TextOverflow.Ellipsis
    )
}

@Preview()
@Composable
fun CardPreview() {
    Card(
        CardInfo(imageID = R.drawable.ic_launcher_background, imageDescription = "Mi lista", title = "Mi lista", subtitle = "Farmacia"),
        Modifier.padding(horizontal = 0.dp, vertical = 5.dp)
    )
}