package com.example.teammaravillaapp.component

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.teammaravillaapp.ui.theme.TeamMaravillaAppTheme
import com.example.teammaravillaapp.util.TAG_GLOBAL

/**
 * Imagen de perfil (real o placeholder).
 *
 * @param imageRes si es `null`, muestra un cuadro con texto.
 */
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
                .clip(RoundedCornerShape(16.dp))
                .clickable { Log.e(TAG_GLOBAL, "Click en imagen de perfil (real)") },
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

@Preview
@Composable
fun PreviewProfileImage() {
    TeamMaravillaAppTheme {
            ProfileImage()
    }
}