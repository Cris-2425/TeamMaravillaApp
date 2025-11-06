package com.example.teammaravillaapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.teammaravillaapp.page.CreateListt
import com.example.teammaravillaapp.ui.theme.TeamMaravillaAppTheme
import com.example.teammaravillaapp.page.Home
import com.example.teammaravillaapp.page.Login
import com.example.teammaravillaapp.page.Profile
import com.example.teammaravillaapp.page.Recipes

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TeamMaravillaAppTheme {
                //
                // CreateListt()
                Home()
                // ListDetail()
                //
                // Login()
                // Profile()
                // Recipes()
                // RecipesDetail()
            }
        }
    }
}