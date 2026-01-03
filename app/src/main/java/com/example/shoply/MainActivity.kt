package com.example.shoply

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.shoply.presentation.navsetup.AppRoot

//bottom bar with navigation to home, product, settings screens
// manager for handling user sessions and preferences
// manager for keyboard visibility and input focus
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AppRoot()
//            HomeScreenLayout(HomeScreenViewModel(), onClickBack = {}, onClickSideMenu = {}, onFabClick = {})
        }
    }
}








