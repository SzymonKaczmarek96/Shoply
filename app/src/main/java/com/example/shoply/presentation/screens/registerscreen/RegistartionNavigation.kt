package com.example.shoply.presentation.screens.registerscreen

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable

@Serializable
data object RegistrationDestination

fun NavGraphBuilder.registrationScreen(
    onSignUpClick: () -> Unit,
    onBackButtonClick: () -> Unit,
) {
    composable<RegistrationDestination> {
        RegistrationScreen(
            onSignUpClick = onSignUpClick,
            onBackButtonClick = onBackButtonClick
        )
    }
}

fun NavController.navigateToRegistrationScreen() {
    navigate(RegistrationDestination) {
        launchSingleTop = true
    }
}