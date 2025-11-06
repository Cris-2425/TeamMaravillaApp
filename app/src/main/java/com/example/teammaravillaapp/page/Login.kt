package com.example.teammaravillaapp.page

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.teammaravillaapp.component.BackButton
import com.example.teammaravillaapp.component.GeneralBackground
import com.example.teammaravillaapp.component.Title
import com.example.teammaravillaapp.ui.theme.TeamMaravillaAppTheme

@Composable
fun Login() {
    Box(
        Modifier.fillMaxSize()
    ) {

        GeneralBackground()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(Modifier.height(28.dp))

            Title("Registrarse")

            Spacer(Modifier.height(160.dp))


            Surface(
                shape = RoundedCornerShape(24.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    OutlinedTextField(
                        value = "",
                        onValueChange = {},
                        placeholder = { Text("Usuario") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp)),
                        singleLine = true
                    )
                    Spacer(Modifier.height(12.dp))

                    OutlinedTextField(
                        value = "",
                        onValueChange = {},
                        placeholder = { Text("Contraseña") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp)),
                        singleLine = true,
                    )
                }
            }
        }

        Box(
            Modifier
                .align(Alignment.BottomStart)
        ) {
            BackButton()
        }
    }
}

@Preview
@Composable
fun PreviewRegister() {
    TeamMaravillaAppTheme {
        Login()
    }
}