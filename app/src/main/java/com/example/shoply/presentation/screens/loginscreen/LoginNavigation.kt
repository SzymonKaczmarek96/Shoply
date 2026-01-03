package com.example.shoply.presentation.screens.loginscreen

import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable

@Serializable
data object LoginDestination

fun NavGraphBuilder.loginScreen(
    onLoginClick: () -> Unit,
    onCreateAccountClick: () -> Unit,
    onGoogleLoginClick: () -> Unit,
    onAppleLoginClick: () -> Unit,
) {
    composable<LoginDestination> {
        LoginScreen(
            modifier = Modifier,
            onLoginClick = onLoginClick,
            onCreateAccountClick = onCreateAccountClick,
            onGoogleLoginClick = onGoogleLoginClick,
            onAppleLoginClick = onAppleLoginClick
        )
    }
}
