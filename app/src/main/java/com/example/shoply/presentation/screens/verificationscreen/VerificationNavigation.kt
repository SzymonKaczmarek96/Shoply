package com.example.shoply.presentation.screens.verificationscreen

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable

@Serializable
data object VerificationDestination


fun NavGraphBuilder.verificationScreen(
    onBackClick: () -> Unit,
) {
    composable<VerificationDestination> {
        VerificationScreen(
            onBackClick = onBackClick
        )
    }
}

fun NavController.navigateToVerificationScreen() {
    navigate(VerificationDestination) {
        launchSingleTop = true
    }
}
