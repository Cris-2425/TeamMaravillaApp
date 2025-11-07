package com.example.teammaravillaapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.teammaravillaapp.page.CategoryFilter
import com.example.teammaravillaapp.page.CreateListt
import com.example.teammaravillaapp.page.Home
import com.example.teammaravillaapp.page.ListDetail
import com.example.teammaravillaapp.page.ListViewTypes
import com.example.teammaravillaapp.page.Login
import com.example.teammaravillaapp.page.Profile
import com.example.teammaravillaapp.page.Recipes
import com.example.teammaravillaapp.page.RecipesDetail
import com.example.teammaravillaapp.ui.theme.TeamMaravillaAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TeamMaravillaAppTheme {
                // CategoryFilter()
                // CreateListt()
                Home()
                // ListDetail()
                // ListViewTypes()
                // Login()
                // Profile()
                // Recipes()
                // RecipesDetail()
            }
        }
    }
}