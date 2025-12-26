package com.example.shoply.presentation.screens.homescreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.shoply.presentation.utils.UiDIm
import com.myapp.shoply.R

//floatig action button and bottom bar are empty for now
@Composable
fun HomeScreen(
    modifier: Modifier
) {
    Scaffold(
        topBar = {
            NavTopBar(
                modifier = Modifier,
                title = "Shoply",
                onClickBack = {},
                onClickSideMenu = {}
            )
        },
        content = { paddingValues ->
            HomeContentScreen(modifier = modifier.padding(paddingValues))
        },
        floatingActionButton = {},
        bottomBar = { }
    )
}


@Composable
private fun HomeContentScreen(
    modifier: Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(color = Color.White)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()

        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(UiDIm.PADDING_MEDIUM),
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Home Screen", fontSize = 28.sp, fontWeight = FontWeight.SemiBold,
                    color = Color(0xff374151)
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(UiDIm.PADDING_MEDIUM),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "My Lists", fontSize = 28.sp, fontWeight = FontWeight.Bold,
                    color = Color(0xff374151)
                )
                Row(
                    modifier = Modifier,
                ) {
                    IconButton(
                        modifier = Modifier
                            .offset(x = (8.dp)),
                        onClick = {}
                    ) {
                        Icon(
                            modifier = Modifier,
                            painter = painterResource(R.drawable.refresh_icon),
                            contentDescription = "Refresh logo",
                            tint = Color(0xff4B5563)

                        )
                    }

                    IconButton(
                        onClick = {}
                    ) {
                        Icon(
                            modifier = Modifier,
                            painter = painterResource(R.drawable.notification_icon),
                            contentDescription = "Refresh logo",
                            tint = Color(0xff4B5563)

                        )
                    }
                }
            }

            LazyColumn(
                modifier = Modifier
            ) {
                items(5) {
                    CardListScreen(modifier, "Grocery Shopping")

                }
            }

        }
    }
}

@Composable
private fun CardListScreen(
    modifier: Modifier,
    title: String,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(UiDIm.PADDING_LARGE)
            .background(color = Color(0xffF9FAFB))
    ) {

        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(UiDIm.PADDING_LARGE),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(title, fontSize = 16.sp, fontWeight = FontWeight.SemiBold)

            Box(
                modifier = Modifier
                    .background(Color(0xff4ADE80), shape = RoundedCornerShape(30.dp))
            ) {
                Text(
                    modifier = Modifier
                        .padding(UiDIm.PADDING_SMALL),
                    text = "Status",
                    color = Color.White,
                    fontSize = 12.sp,
                )
            }
        }

        Row(
            modifier = Modifier
                .fillMaxSize()
                .offset(y = (-10).dp)
                .padding(start = UiDIm.PADDING_LARGE),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("3/12 items purchased")
//            AsyncImage() TODO
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun NavTopBar(
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
    NavTopBar(
        modifier = Modifier,
        title = "Shoply",
        onClickBack = {},
        onClickSideMenu = {}
    )
}

@Preview
@Composable
fun HomeScreenPreview() {
    HomeScreen(modifier = Modifier)
}

@Preview
@Composable
fun HomeContentScreenPreview() {
    HomeContentScreen(modifier = Modifier)
}

@Preview
@Composable
fun CardListScreenPreview() {
    CardListScreen(modifier = Modifier, "Grocery Shopping")
}