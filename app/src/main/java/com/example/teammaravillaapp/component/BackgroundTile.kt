package com.example.teammaravillaapp.component

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.teammaravillaapp.R
import com.example.teammaravillaapp.util.TAG_GLOBAL

@Composable
fun BackgroundTile(
    selected: Boolean = false,
    label: String,
    onClick: () -> Unit = {}
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
                width = if (selected)

                    2.dp else 1.dp,

                color = if (selected)

                    MaterialTheme.colorScheme.primary

                else

                    MaterialTheme.colorScheme.secondary,

                shape = RoundedCornerShape(12.dp)
            )
            .clickable {
                Log.e(TAG_GLOBAL, "Fondo pulsado: $label (selected=$selected)")
                onClick()
            },
        contentAlignment = Alignment.Center

    ) {
        if (imageRes != null) {
            Image(
                painter = painterResource(id = imageRes),
                contentDescription = "",
                modifier = Modifier
                    .matchParentSize(),
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
        Text(
            label,
            color = MaterialTheme.colorScheme.onBackground
        )
    }
}