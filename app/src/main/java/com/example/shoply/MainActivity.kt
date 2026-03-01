package com.example.shoply

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.shoply.presentation.navigation.AppRoot


// shown only categories which are existsing in product list
// problem with selected new item
//problem with not passing the selected product after filtering the product list by category
// problem with selecting product
// basic validation by product name
// split products by categories
// product catalog screen
// dialog or snackbar for the incorrect data entry and successfull account creation
// product list - UI/UX
// product list screen
// setting screen layout - UI/UX
// settings screen
// side menu / drawer layout  - UI/UX
// side menu / drawer screen
// manager for handling user sessions and preferences
// manager for keyboard visibility and input focus
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AppRoot()
        }
    }
}








