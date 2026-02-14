package com.example.shoply.presentation.screens.verificationscreen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.shoply.presentation.components.ShoplyTopBar
import com.example.shoply.presentation.utils.UiDim
import com.myapp.shoply.R

@Composable
fun VerificationScreen(
    onBackClick: () -> Unit,
) {
    RootView(
        onBackClick = onBackClick
    )
}

@Composable
private fun RootView(
    onBackClick: () -> Unit
) {
    VerificationScreenLayout(
        modifier = Modifier,
        onBackClick = onBackClick
    )
}

@Composable
private fun VerificationScreenLayout(
    modifier: Modifier,
    onBackClick: () -> Unit,
) {
    Scaffold(
        contentWindowInsets = WindowInsets(0),
        topBar = {
            ShoplyTopBar(
                modifier = Modifier,
                title = "Shoply",
                onBackButtonClick = onBackClick,
                isLoginScreen = true
            )
        },
    ) { padding ->
        VerificationContentScreen(
            modifier = modifier.padding(
                top = padding.calculateTopPadding(),
                bottom = padding.calculateBottomPadding(),
            )
        )
    }
}


@Composable
private fun VerificationContentScreen(
    modifier: Modifier,
) {
    var text123 by remember { mutableStateOf("") }
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(color = Color.White)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {

            Box(
                modifier = Modifier
                    .size(64.dp)
                    .clip(shape = CircleShape)
                    .background(color = Color(0xff6B73FF))
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.email_icon),
                    contentDescription = "Login Icon",
                    tint = Color(0xffFfffff),
                    modifier = Modifier
                        .padding(UiDim.PADDING_LARGE)
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Check Your Email", fontWeight = FontWeight.SemiBold, fontSize = 24.sp,
                color = Color(0xff111827)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "We've sent a verification code to",
                fontWeight = FontWeight.Normal,
                fontSize = 16.sp,
                color = Color(0xff6B7280)
            )
            Text(
                text = "your email address",
                fontWeight = FontWeight.Normal,
                fontSize = 16.sp,
                color = Color(0xff6B7280)
            )

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = "Enter Verification Code",
                fontWeight = FontWeight.SemiBold,
                fontSize = 16.sp,
                color = Color(0xff111827)
            )

            LazyRow() {
                items(6) {
                    OutlinedTextField(
                        modifier = Modifier
                            .size(54.dp, 80.dp)
                            .padding(end = UiDim.PADDING_SMALL, top = UiDim.PADDING_LARGE),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        value = text123,
                        maxLines = 1,
                        onValueChange = { text123 = it },
                    )
                }
            }
            Spacer(modifier = Modifier.height(8.dp))

            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = UiDim.PADDING_LARGE)
                    .height(50.dp),
                onClick = {},
                shape = RoundedCornerShape(12.dp),
                colors = ButtonColors(
                    containerColor = Color(0xffFF0080),
                    contentColor = Color(0xffFFFFFF),
                    disabledContentColor = Color(0xffFFFFFF),
                    disabledContainerColor = Color(0xffFFFFFF)
                )
            ) {
                Text(text = "Verify", fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = UiDim.PADDING_LARGE),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Didn't receive the code?",
                    color = Color(0xff6B7280),
                    fontSize = 16.sp,
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    modifier = Modifier
                        .padding(start = UiDim.PADDING_SMALL)
                        .clickable {
                            //TODO: Resend code action
                        },
                    text = "Resend Code",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 20.sp,
                    color = Color(0xffFF0080),

                    )
            }
        }
    }
}


@Preview
@Composable
fun VerificationScreenPreview() {
    VerificationScreen({})
}