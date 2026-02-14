package com.example.shoply.presentation.screens.registerscreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.shoply.presentation.components.ShoplyTopBar
import com.example.shoply.presentation.utils.UiDim


@Composable
fun RegistrationScreen(
    onSignUpClick: () -> Unit,
    onBackButtonClick: () -> Unit,
) {
    RootView(
        onSignUpClick = onSignUpClick,
        onBackButtonClick = onBackButtonClick
    )
}

@Composable
private fun RootView(
    onSignUpClick: () -> Unit,
    onBackButtonClick: () -> Unit
) {
    RegistrationScreenLayout(
        modifier = Modifier,
        onSignUpClick = onSignUpClick,
        onBackButtonClick = onBackButtonClick
    )
}

@Composable
private fun RegistrationScreenLayout(
    modifier: Modifier,
    onSignUpClick: () -> Unit,
    onBackButtonClick: () -> Unit,
) {
    Scaffold(
        topBar = {
            ShoplyTopBar(
                modifier = Modifier,
                title = "Shoply",
                onBackButtonClick = onBackButtonClick,
                isLoginScreen = true
            )
        },
        content = { innerPadding ->
            RegistrationContentScreen(
                modifier = modifier.padding(innerPadding),
                onSignUpClick = onSignUpClick
            )
        }
    )
}

@Composable
private fun RegistrationContentScreen(
    modifier: Modifier,
    onSignUpClick: () -> Unit,
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(color = Color.White)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(UiDim.PADDING_LARGE),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Create Account", fontWeight = FontWeight.SemiBold, fontSize = 24.sp,
                color = Color(0xff111827)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Join Shoply today",
                fontWeight = FontWeight.Normal,
                fontSize = 16.sp,
                color = Color(0xff6B7280)
            )

            Spacer(modifier = Modifier.height(32.dp))

            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = UiDim.PADDING_LARGE),
                value = "",
                maxLines = 1,
                onValueChange = { it },
                label = { Text(text = "Email") },
            )

            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = UiDim.PADDING_LARGE),
                value = "",
                maxLines = 1,
                onValueChange = { },
                label = { Text(text = "Password") },
                visualTransformation = PasswordVisualTransformation()
            )

            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = UiDim.PADDING_LARGE),
                value = "",
                maxLines = 1,
                onValueChange = { },
                label = { Text(text = "Confirm Password") },
                visualTransformation = PasswordVisualTransformation()
            )

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = UiDim.PADDING_LARGE)
                    .height(50.dp),
                onClick = onSignUpClick,
                shape = RoundedCornerShape(12.dp),
                colors = ButtonColors(
                    containerColor = Color(0xffFF0080),
                    contentColor = Color(0xffFFFFFF),
                    disabledContentColor = Color(0xffFFFFFF),
                    disabledContainerColor = Color(0xffFFFFFF)
                )
            ) {
                Text(text = "Sign up", fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = UiDim.PADDING_LARGE),
                horizontalArrangement = Arrangement.Center
            )
            {
                Text(
                    text = "By signing up, you agree to our Terms of Service and",
                    fontSize = 12.sp,
                    color = Color(0xff6B7280)
                )
            }
            Row() {
                Text(
                    text = "Privacy Policy",
                    fontSize = 12.sp,
                    color = Color(0xff6B7280),
                )
            }
        }
    }
}

@Preview
@Composable
fun RegistrationScreenPreview() {
    RegistrationContentScreen(
        modifier = Modifier,
        onSignUpClick = {},
    )
}