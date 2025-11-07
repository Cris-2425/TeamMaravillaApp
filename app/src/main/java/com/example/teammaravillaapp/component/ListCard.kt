package com.example.teammaravillaapp.component

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import com.example.teammaravillaapp.ui.theme.TeamMaravillaAppTheme
import com.example.teammaravillaapp.util.TAG_GLOBAL

data class CardInfo(
    val imageID: Int,
    val imageDescription: String = "",
    val title: String,
    val subtitle: String
)

@Composable
fun ListCard(
    cardInfo: CardInfo,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .size(width = 240.dp, height = 100.dp)
            .clickable {
                Log.e(TAG_GLOBAL, "Lista: ${cardInfo.title}")
            }
    ) {
        Row(
            Modifier
                .padding(8.dp)) {
            Image(
                painter = painterResource(id = cardInfo.imageID),
                contentDescription = cardInfo.imageDescription,
                modifier = Modifier
                    .size(64.dp)
                    .padding(end = 8.dp)
            )
            Column(
                Modifier
                    .fillMaxWidth()
            ) {
                Text(
                    text = cardInfo.title,
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(Modifier.height(2.dp))

                Text(
                    text = cardInfo.subtitle,
                    color = MaterialTheme.colorScheme.secondary,
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

@Preview
@Composable
fun ListCardPreview() {
    TeamMaravillaAppTheme {
        ListCard(
            CardInfo(
                imageID = R.drawable.list_supermarket,
                imageDescription = "Mi lista",
                title = "Mi lista",
                subtitle = "Farmacia"
            ),
            modifier = Modifier
                .padding(6.dp)
        )
    }
}