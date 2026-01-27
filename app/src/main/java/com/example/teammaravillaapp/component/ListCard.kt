package com.example.teammaravillaapp.component

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.teammaravillaapp.R
import com.example.teammaravillaapp.model.CardInfo
import com.example.teammaravillaapp.ui.theme.TeamMaravillaAppTheme
import com.example.teammaravillaapp.util.TAG_GLOBAL

@Composable
fun ListCard(
    cardInfo: CardInfo,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    val cs = MaterialTheme.colorScheme

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .clip(MaterialTheme.shapes.large)
            .clickable {
                Log.d(TAG_GLOBAL, "Lista: ${cardInfo.title}")
                onClick()
            },
        shape = MaterialTheme.shapes.large,
        color = cs.surfaceContainerLow,
        tonalElevation = 4.dp
    ) {
        Row(
            Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = cs.surfaceVariant,
                modifier = Modifier.size(64.dp),
                tonalElevation = 0.dp
            ) {
                Image(
                    painter = painterResource(id = cardInfo.imageID),
                    contentDescription = cardInfo.imageDescription,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(6.dp)
                )
            }

            Column(Modifier.weight(1f)) {
                Text(
                    text = cardInfo.title,
                    style = MaterialTheme.typography.titleMedium,
                    color = cs.onSurface,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(Modifier.height(2.dp))
                Text(
                    text = cardInfo.subtitle,
                    style = MaterialTheme.typography.bodyMedium,
                    color = cs.onSurfaceVariant,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

@Preview
@Composable
fun PreviewListCard() {
    TeamMaravillaAppTheme {
        ListCard(
            CardInfo(
                imageID = R.drawable.list_supermarket,
                imageDescription = "Lista supermercado",
                title = "Compra semanal",
                subtitle = "2/12 comprados"
            ),
            modifier = Modifier.padding(16.dp)
        )
    }
}