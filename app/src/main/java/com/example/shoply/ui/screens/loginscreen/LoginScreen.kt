package com.example.shoply.ui.screens.loginscreen


import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.shoply.ui.utils.UiDIm
import com.myapp.shoply.R

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun LoginScreen(modifier: Modifier) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(color = Color.White)
    ) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(UiDIm.PADDING_LARGE),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {

            Row(
                modifier = Modifier
                    .padding(UiDIm.PADDING_MEDIUM),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Login Screen", fontWeight = FontWeight.SemiBold, fontSize = 18.sp,
                    color = Color(0xff374151)
                )
            }
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .clip(shape = CircleShape)
                    .background(color = Color(0xffFF0080))
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.cart),
                    contentDescription = "Login Icon",
                    tint = Color(0xffFfffff),
                    modifier = Modifier
                        .padding(UiDIm.PADDING_LARGE)
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Shoply", fontWeight = FontWeight.SemiBold, fontSize = 24.sp,
                color = Color(0xff111827)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Collaborative Shopping Lists",
                fontWeight = FontWeight.Normal,
                fontSize = 16.sp,
                color = Color(0xff6B7280)
            )

            Spacer(modifier = Modifier.height(32.dp))

            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = "",
                maxLines = 1,
                onValueChange = { it },
                label = { Text(text = "Email") },
            )

            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = UiDIm.PADDING_LARGE),
                value = "",
                maxLines = 1,
                onValueChange = { },
                label = { Text(text = "Password") },
                visualTransformation = PasswordVisualTransformation()
            )

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = UiDIm.PADDING_LARGE)
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
                Text(text = "Log in", fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
            }

            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = UiDIm.PADDING_LARGE)
                    .height(50.dp),
                onClick = {},
                shape = RoundedCornerShape(12.dp),
                colors = ButtonColors(
                    containerColor = Color(0xffFFFFFF),
                    contentColor = Color(0xffFFFFFF),
                    disabledContentColor = Color(0xffFFFFFF),
                    disabledContainerColor = Color(0xffFFFFFF)
                ),
                border = BorderStroke(1.dp, Color(0xffBDBDBD))
            ) {
                Text(
                    text = "Create Account", fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold, color = Color(0xff374151)
                )
            }

            Row(
                modifier = Modifier
                    .padding(UiDIm.PADDING_SMALL)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Button(
                    modifier = Modifier
                        .fillMaxWidth(0.5f)
                        .padding(top = UiDIm.PADDING_LARGE)
                        .padding(end = UiDIm.PADDING_SMALL)
                        .height(50.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonColors(
                        containerColor = Color(0xff3B82F6),
                        contentColor = Color(0xff3B82F6),
                        disabledContentColor = Color(0xffFFFFFF),
                        disabledContainerColor = Color(0xffFFFFFF)
                    ),
                    border = BorderStroke(1.dp, Color(0xffBDBDBD)),
                    onClick = {},
                ) {

                    Row(
                        modifier = Modifier
                            .padding(UiDIm.PADDING_SMALL)
                            .height(48.dp),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Icon(
                            painter = painterResource(id = R.drawable.google_logo),
                            contentDescription = "Google Icon",
                            tint = Color(0xffFFFFFF),
                            modifier = Modifier
                                .padding(end = UiDIm.PADDING_SMALL)
                        )

                        Text(
                            modifier = Modifier,
                            text = "Google", fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold, color = Color(0xffFFFFFF)
                        )
                    }


                }

                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = UiDIm.PADDING_LARGE)
                        .padding(start = UiDIm.PADDING_SMALL)
                        .height(50.dp),
                    onClick = {},
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonColors(
                        containerColor = Color(0xff1F2937),
                        contentColor = Color(0xff1F2937),
                        disabledContentColor = Color(0xffFFFFFF),
                        disabledContainerColor = Color(0xffFFFFFF)
                    ),
                    border = BorderStroke(1.dp, Color(0xffBDBDBD))
                ) {


                    Icon(
                        painter = painterResource(id = R.drawable.apple_logo),
                        contentDescription = "Google Icon",
                        tint = Color(0xffFFFFFF),
                        modifier = Modifier
                            .padding(end = UiDIm.PADDING_SMALL)
                    )


                    Text(
                        text = "Apple", fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold, color = Color(0xffFFFFFF)
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun LoginScreenPreview() {
    LoginScreen(modifier = Modifier)
}