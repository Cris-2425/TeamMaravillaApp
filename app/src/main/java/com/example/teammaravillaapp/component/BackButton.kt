package com.example.teammaravillaapp.component

import android.util.Log
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.teammaravillaapp.R
import com.example.teammaravillaapp.ui.theme.TeamMaravillaAppTheme
import com.example.teammaravillaapp.util.TAG_GLOBAL

/**
 * Botón flotante de volver atrás.
 *
 * @param onClick acción al pulsar (usualmente navController.navigateUp()).
 */
@Composable
fun BackButton(
    onClick: () -> Unit = {}
) {
    FloatingActionButton(
        onClick = {
            Log.d(TAG_GLOBAL, "Volver atrás")
            onClick()
        },
        containerColor = MaterialTheme.colorScheme.secondary,
        contentColor = MaterialTheme.colorScheme.onSecondary,
        modifier = Modifier.padding(start = 16.dp, bottom = 120.dp)
    ) {
        Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
            contentDescription = stringResource(R.string.back_button_content_description)
        )
    }
}

@Preview
@Composable
fun PreviewBackButton() {
    TeamMaravillaAppTheme {
        BackButton()
    }
}