package com.example.shoply.presentation.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShoplyTopBar(
    modifier: Modifier,
    title: String,
    onClickBack: () -> Unit = {},
    onClickSideMenu: () -> Unit = {},
) {
    CenterAlignedTopAppBar(
        title = {
            Text(
                text = title,
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
        },
        navigationIcon = {
            IconButton(onClick = { /* TODO */ }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back Icon",
                    tint = Color.Black,
                )
            }
        },
        modifier = modifier,
        actions = {
            IconButton(
                onClick = { /* TODO */ }
            ) {
                Icon(
                    imageVector = Icons.Default.MoreVert,
                    contentDescription = "Side Menu Icon",
                    tint = Color.Black,
                )
            }
        },
    )
}

@Preview
@Composable
fun NavTopBarPreview() {
    ShoplyTopBar(
        modifier = Modifier,
        title = "Shoply",
        onClickBack = {},
        onClickSideMenu = {}
    )
}