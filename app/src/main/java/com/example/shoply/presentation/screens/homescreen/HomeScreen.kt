package com.example.shoply.presentation.screens.homescreen

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
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.shoply.domain.model.ProductList
import com.example.shoply.domain.model.Role
import com.example.shoply.domain.model.User
import com.example.shoply.domain.usecase.GetProductListUseCase
import com.example.shoply.presentation.components.ShoplyFab
import com.example.shoply.presentation.components.ShoplyTopBar
import com.example.shoply.presentation.utils.UiDIm
import com.myapp.shoply.R

@Composable
fun HomeScreen(
    homeScreenViewModel: HomeScreenViewModel,
    onClickBack: () -> Unit,
    onClickSideMenu: () -> Unit,
    onFabClick: () -> Unit
) {
    RootView(
        uiState = homeScreenViewModel.state.collectAsState().value,
        onClickBack = onClickBack,
        onClickSideMenu = onClickSideMenu,
        onFabClick = onFabClick
    )
}

@Composable
private fun RootView(
    uiState: HomeScreenViewModel.State,
    onClickBack: () -> Unit,
    onClickSideMenu: () -> Unit,
    onFabClick: () -> Unit
) {
    HomeScreen(
        uiState = uiState,
        modifier = Modifier,
        onClickBack = onClickBack,
        onClickSideMenu = onClickSideMenu,
        onFabClick = onFabClick
    )
}

@Composable
private fun HomeScreen(
    uiState: HomeScreenViewModel.State,
    modifier: Modifier,
    onClickBack: () -> Unit,
    onClickSideMenu: () -> Unit,
    onFabClick: () -> Unit
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            ShoplyTopBar(
                modifier = Modifier,
                title = stringResource(R.string.app_name),
                onClickBack = onClickBack,
                onClickSideMenu = onClickSideMenu
            )
        },
        content = { paddingValues ->
            HomeContentScreen(
                modifier = Modifier.padding(paddingValues),
                uiState = uiState
            )
        },
        floatingActionButton = {
            ShoplyFab(modifier = Modifier, onFabClick = onFabClick)
        },
        floatingActionButtonPosition = FabPosition.Center,
        bottomBar = { }
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
                        .padding(UiDIm.PADDING_SMALL),
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
                .offset(y = (-20).dp)
                .padding(start = UiDIm.PADDING_LARGE),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
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
                .padding(start = UiDIm.PADDING_LARGE),
            verticalAlignment = Alignment.CenterVertically
        ) {
            MembersImageList(productList.members)
            Text(
                productList.members.size.toString() + " members",
                modifier = Modifier
                    .padding(start = UiDIm.PADDING_SMALL),
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
    HomeScreen(
        modifier = Modifier,
        uiState = HomeScreenViewModel.State(
            products = GetProductListUseCase().productList
        ),
        onClickBack = {},
        onClickSideMenu = {},
        onFabClick = {},
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