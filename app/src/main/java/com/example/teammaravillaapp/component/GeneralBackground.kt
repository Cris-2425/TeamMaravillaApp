package com.example.teammaravillaapp.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.teammaravillaapp.R
import com.example.teammaravillaapp.ui.theme.TeamMaravillaAppTheme

@Composable
fun GeneralBackground(
) {
    Image(
        painter = painterResource(id = R.drawable.background_app_final),
        contentDescription = "",
        modifier = Modifier.fillMaxSize(),
        contentScale = ContentScale.Crop
    )
}

@Preview
@Composable
fun PreviewGeneralBackground() {
    TeamMaravillaAppTheme {
        GeneralBackground()
    }
}