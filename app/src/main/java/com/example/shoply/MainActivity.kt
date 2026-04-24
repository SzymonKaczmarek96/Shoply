package com.example.shoply

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.shoply.presentation.navigation.AppRoot

// currently home screen keep card item list,
// perhaps could be better to create separate lazy list with productListScreen and transfer into the content
// product list navigation
// product list viewModel
// product list screen
// open product list on click
// add product list with dialog
// map Product into the UiProduct in ViewModel
// how to move product list directly on the screen
// after onDelete click show a dialog to confirm the deletion of the item
// Add posibility to add products to the shoply list and remove them
// product list - UI/UX
// product list screen
// setting screen layout - UI/UX
// settings screen
// side menu / drawer layout  - UI/UX
// side menu / drawer screen
// dialog or snackbar for the incorrect data entry and successfull account creation
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








