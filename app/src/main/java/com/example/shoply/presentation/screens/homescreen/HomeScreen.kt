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
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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

// That is require scaffold for this implementation?
@Composable
fun HomeScreen(
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
                items(5) { item ->
                    CardListScreen(modifier, item, "Grocery Shopping")

                }
            }

        }
    }
}

@Composable
fun CardListScreen(
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
//            AsyncImage()
        }
    }

}


@Preview
@Composable
fun HomeScreenPreview() {
    HomeScreen(modifier = Modifier)
}

@Preview
@Composable
fun CardListScreenPreview() {
    CardListScreen(modifier = Modifier, 1, "Grocery Shopping")
}