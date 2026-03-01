package com.example.shoply.presentation.screens.homescreen

import FabConfig
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.shoply.domain.model.ProductList
import com.example.shoply.domain.model.Role
import com.example.shoply.domain.model.User
import com.example.shoply.domain.usecase.GetProductListUseCase
import com.example.shoply.presentation.utils.UiDim
import com.myapp.shoply.R
import org.koin.androidx.compose.koinViewModel


@Composable
fun HomeScreen(
    viewModel: HomeScreenViewModel = koinViewModel(),
    onFabConfigChange: (FabConfig) -> Unit
) {
    RootView(
        uiState = viewModel.state.collectAsState().value,
    )
}

@Composable
private fun RootView(
    uiState: HomeScreenViewModel.State,
) {
    HomeScreenLayout(
        uiState = uiState
    )
}

@Composable
private fun HomeScreenLayout(
    uiState: HomeScreenViewModel.State,
) {
    HomeContentScreen(
        modifier = Modifier,
        uiState = uiState
    )

}

@Composable
private fun HomeContentScreen(
    modifier: Modifier,
    uiState: HomeScreenViewModel.State
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
                    .padding(UiDim.PADDING_MEDIUM),
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
                modifier = Modifier,
            ) {
                items(uiState.products ?: emptyList()) { product ->
                    CardListScreen(
                        modifier = Modifier,
                        productList = product,
                    )

                }

            }
        }
    }
}

@Composable
private fun CardListScreen(
    modifier: Modifier,
    productList: ProductList
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(UiDim.PADDING_LARGE)
            .background(color = Color(0xffF9FAFB))
    ) {

        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    bottom = UiDim.PADDING_SMALL,
                    top = UiDim.PADDING_MEDIUM,
                    start = UiDim.PADDING_LARGE,
                    end = UiDim.PADDING_LARGE
                ),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(productList.name, fontSize = 16.sp, fontWeight = FontWeight.SemiBold)

            Box(
                modifier = Modifier
                    .background(
                        if (productList.isComplete)
                            Color(0xFFFABB02) else Color(0xff4ADE80),
                        shape = RoundedCornerShape(30.dp)
                    )
            ) {
                Text(
                    modifier = Modifier
                        .padding(UiDim.PADDING_SMALL),
                    text = if (productList.isComplete) {
                        "Active"
                    } else {
                        "Completed"
                    },
                    color = Color.White,
                    fontSize = 12.sp,
                )
            }
        }

        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = UiDim.PADDING_LARGE, bottom = UiDim.PADDING_LARGE),
        ) {
            Text(
                "${productList.quantityOfPurchasedProducts} " +
                        "/ ${productList.quantityOfProducts}" +
                        " items purchased",
                color = Color.Gray
            )
        }

        Row(
            modifier = Modifier
                .fillMaxSize()
                .offset(y = (-10).dp)
                .padding(start = UiDim.PADDING_LARGE),
            verticalAlignment = Alignment.CenterVertically
        ) {
            MembersImageList(productList.members)
            Text(
                productList.members.size.toString() + " members",
                modifier = Modifier
                    .padding(start = UiDim.PADDING_SMALL),
                fontSize = 12.sp,
                color = Color.Gray
            )
        }
    }
}

@Composable
private fun MembersImageList(
    users: List<User>
) {
    Row(
        modifier = Modifier,
    ) {
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy((-12).dp)
        ) {
            items(users) { user ->
                AsyncImage(
                    model = user.profilePictureUrl,
                    contentDescription = user.name,
                    modifier = Modifier
                        .size(42.dp)
                        .clip(CircleShape)
                        .border(2.dp, Color.White, CircleShape)
                )
            }
        }
    }
}

@Preview
@Composable
fun HomeScreenPreview() {
    HomeScreenLayout(
        uiState = HomeScreenViewModel.State(
            products = GetProductListUseCase().productList
        ),
    )
}

@Preview
@Composable
fun HomeContentScreenPreview() {
    HomeContentScreen(
        modifier = Modifier,
        uiState = HomeScreenViewModel.State(
            products = GetProductListUseCase().productList
        )
    )
}

@Preview
@Composable
fun CardListScreenPreview() {
    CardListScreen(
        modifier = Modifier,
        productList = GetProductListUseCase().productList.first(),
    )
}

@Preview
@Composable
fun MembersImageListPreview() {
    MembersImageList(
        users = listOf(
            User(
                name = "Alice Johnson",
                email = "123@gmail.com",
                role = Role.CREATOR,
            ), User(
                name = "Alice Woman",
                email = "123@gmail.com",
                role = Role.MEMBER,
            ),
            User(
                name = "Alice Man",
                email = "123@gmail.com",
                role = Role.MEMBER,
            )
        )
    )
}