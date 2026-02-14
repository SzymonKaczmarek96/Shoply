package com.example.shoply

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.shoply.presentation.navigation.AppRoot

// dialog manager after FAB click to add new product
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








