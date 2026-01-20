package com.example.teammaravillaapp.page.profile

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.teammaravillaapp.R
import com.example.teammaravillaapp.component.BackButton
import com.example.teammaravillaapp.component.GeneralBackground
import com.example.teammaravillaapp.component.OptionsGrid
import com.example.teammaravillaapp.component.ProfileImage
import com.example.teammaravillaapp.model.ProfileOption
import com.example.teammaravillaapp.ui.theme.TeamMaravillaAppTheme
import com.example.teammaravillaapp.util.TAG_GLOBAL

@Composable
fun Profile(
    onBack: () -> Unit,
    onNavigate: (ProfileOption) -> Unit = {},
    // ✅ NUEVO
    username: String? = null,
    isLoggedIn: Boolean = false,
    onLogout: () -> Unit = {}
) {
    Box(Modifier.fillMaxSize()) {

        GeneralBackground()

        ProfileContent(
            username = username,
            isLoggedIn = isLoggedIn,
            onLogout = onLogout,
            onOptionClick = { option ->
                Log.d(TAG_GLOBAL, "Profile → Click en opción: $option")
                onNavigate(option)
            }
        )

        Box(Modifier.align(Alignment.BottomStart)) {
            BackButton(onClick = {
                Log.d(TAG_GLOBAL, "Profile → BackButton")
                onBack()
            })
        }
    }
}

@Composable
fun ProfileContent(
    username: String?,
    isLoggedIn: Boolean,
    onLogout: () -> Unit,
    onOptionClick: (ProfileOption) -> Unit
) {
    val options = ProfileOption.entries.toTypedArray()

    Column(
        Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.height(44.dp))

        ProfileImage(
            imageRes = null,
            modifier = Modifier.clickable {
                Log.d(TAG_GLOBAL, "Profile → Click en imagen")
            }
        )

        Spacer(Modifier.height(30.dp))

        Text(
            text = username ?: stringResource(R.string.profile_username_placeholder),
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onBackground
        )

        Spacer(Modifier.height(64.dp))

        OptionsGrid(
            options = options.map { stringResource(it.labelRes) },
            onOptionClick = { index ->
                onOptionClick(options[index])
            }
        )

        // ✅ Logout (solo si está logueado)
        if (isLoggedIn) {
            Spacer(Modifier.height(22.dp))

            Text(
                text = "Cerrar sesión",
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier
                    .clickable {
                        Log.d(TAG_GLOBAL, "Profile → Logout")
                        onLogout()
                    }
                    .padding(vertical = 8.dp)
            )
        }
    }
}

@Preview
@Composable
fun PreviewProfile() {
    TeamMaravillaAppTheme {
        Profile(
            onBack = {},
            onNavigate = {},
            username = "juan",
            isLoggedIn = true,
            onLogout = {}
        )
    }
}