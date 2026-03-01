package com.example.shoply.presentation.screens.productcatalogscreen

import FabConfig
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddShoppingCart
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.shoply.domain.model.Product
import com.example.shoply.domain.model.ProductCategory
import com.example.shoply.presentation.components.dialogs.DialogLayout
import com.example.shoply.presentation.mapper.ProductCategoryIconMapper
import com.example.shoply.presentation.utils.UiDim
import org.koin.androidx.compose.koinViewModel
import java.util.UUID

@Composable
@ExperimentalMaterial3Api
fun ProductCatalogScreen(
    modifier: Modifier,
    viewModel: ProductCatalogScreenViewModel =
        koinViewModel(),
    onFabConfigChange: (FabConfig) -> Unit,
) {
    val uiState = viewModel.state.collectAsState().value

    LaunchedEffect(uiState) {
        onFabConfigChange(
            FabConfig(
                visible = true,
                onClick = {
                    viewModel.openAddProductDialog()
                }
            )
        )
    }

    RootView(
        modifier = modifier,
        uiState = uiState,
        onCheckboxClick = { productId, isChecked ->
            viewModel.onProductCheckedChange(productId, isChecked)
        },
        onSearchQuery = { viewModel.findProduct(it) },
        onCategoryClick = { viewModel.onCategorySelected(it) }
    )

    DialogLayout(
        dialogState = uiState.dialog,
        modifier = Modifier,
        onDismiss = viewModel::dismissDialog,
        onValueChange = viewModel::onDialogNameChange,
        onCategorySelected = viewModel::onDialogCategorySelected,
        onConfirm = viewModel::onDialogConfirm
    )
}

@Composable
@ExperimentalMaterial3Api
private fun RootView(
    modifier: Modifier,
    uiState: ProductCatalogScreenViewModel.State,
    onCheckboxClick: (UUID, Boolean) -> Unit,
    onSearchQuery: (String) -> Unit,
    onCategoryClick: (ProductCategory) -> Unit
) {
    ProductCatalogLayout(
        modifier = modifier,
        uiState = uiState,
        onCheckboxClick = onCheckboxClick,
        onSearchQuery = onSearchQuery,
        onCategoryClick = onCategoryClick
    )
}


@Composable
@ExperimentalMaterial3Api
private fun ProductCatalogLayout(
    modifier: Modifier,
    uiState: ProductCatalogScreenViewModel.State,
    onCheckboxClick: (UUID, Boolean) -> Unit,
    onSearchQuery: (String) -> Unit,
    onCategoryClick: (ProductCategory) -> Unit
) {

    var value by remember { mutableStateOf("") }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(UiDim.PADDING_MEDIUM)

        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Products Catalog",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                )
                Row(
                    modifier = Modifier,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        modifier = Modifier
                            .size(UiDim.ICON_SIZE),
                        onClick = {}
                    ) {
                        Icon(
                            modifier = Modifier,
                            imageVector = Icons.Default.AddShoppingCart,
                            contentDescription = "Refresh logo",
                            tint = Color(0xff4B5563)
                        )

                    }
                    IconButton(
                        modifier = Modifier
                            .size(32.dp),
                        onClick = {}
                    ) {
                        Icon(
                            modifier = Modifier,
                            imageVector = Icons.Default.Refresh,
                            contentDescription = "Refresh logo",
                            tint = Color(0xff4B5563)
                        )
                    }
                    IconButton(
                        modifier = Modifier
                            .size(32.dp),
                        onClick = {}
                    ) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Delete Icon",
                            tint = Color(0xff4B5563)
                        )
                    }
                }
            }
            Row(
                modifier = Modifier
                    .padding(vertical = UiDim.PADDING_LARGE)
            ) {
                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFFF9FAFB)),
                    value = value,
                    onValueChange = {
                        value = it
                        onSearchQuery.invoke(it)
                    },
                    placeholder = {
                        Text("Find item...")
                    },
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent
                    ),
                    trailingIcon = {
                        IconButton(
                            modifier = Modifier
                                .size(32.dp),
                            onClick = {
                                //FIXME
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = "Search Icon",
                                tint = Color(0xff4B5563)
                            )
                        }
                    }
                )
            }
            CategoryRow(
                uiState = uiState,
                onCategoryClick = onCategoryClick
            )
            ProductCatalogItem(
                uiState = uiState,
                onCheckboxClick = onCheckboxClick
            )
        }
    }
}

@Composable
@ExperimentalMaterial3Api
private fun CategoryRow(
    uiState: ProductCatalogScreenViewModel.State,
    onCategoryClick: (ProductCategory) -> Unit,
) {
    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = UiDim.PADDING_SMALL, bottom = UiDim.PADDING_LARGE)
    ) {

        items(uiState.productCategories) { item ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .padding(end = UiDim.PADDING_MEDIUM)
                    .clickable {
                        onCategoryClick.invoke(item)
                    }
                    .background(
                        color = if (uiState.selectedCategory.name == item.name)
                            Color(0xffFF0080) else Color(0xFFF5F5F5),
                        shape = RoundedCornerShape(12.dp)
                    )
                    .padding(vertical = UiDim.PADDING_MEDIUM)
            ) {
                Icon(
                    modifier = Modifier
                        .padding(horizontal = UiDim.PADDING_MEDIUM),
                    imageVector = ProductCategoryIconMapper.iconForCategory(item),
                    contentDescription = "Category Icon",
                    tint = if (uiState.selectedCategory.name == item.name)
                        Color(0xFFFFFFFF) else Color(0xFF8F8F8F),
                )
                Text(
                    text = item.name.lowercase().replaceFirstChar { it.uppercase() },
                    modifier = Modifier
                        .padding(end = UiDim.PADDING_LARGE),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = if (uiState.selectedCategory.name == item.name)
                        Color(0xFFFFFFFF) else Color(0xFF8F8F8F)
                )
            }
        }
    }
}


@Composable
@ExperimentalMaterial3Api
private fun ProductCatalogItem(
    uiState: ProductCatalogScreenViewModel.State,
    onCheckboxClick: (UUID, Boolean) -> Unit,
) {
    LazyColumn {
        items(uiState.products) { item ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(UiDim.PADDING_SMALL)
                    .background(
                        color = Color(0xFFF5F5F5),
                        shape = RoundedCornerShape(12.dp)
                    ),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Checkbox(
                        checked = item.isSelected,
                        onCheckedChange = {
                            onCheckboxClick.invoke(
                                item.uuid, it
                            )
                        },
                        colors = CheckboxDefaults.colors(
                            checkedColor = Color.Green,
                            uncheckedColor = Color.DarkGray,
                            checkmarkColor = Color.White
                        )
                    )
                    Text(
                        text = item.name,
                        fontSize = 16.sp,
//                        textDecoration = if (item.isPurchased) TextDecoration.LineThrough else null,
                        fontWeight = FontWeight.Normal,
                        color = Color(0xff111827)
                    )
                }
                Box {
                    Icon(
                        modifier = Modifier
                            .padding(horizontal = UiDim.PADDING_LARGE),
                        imageVector = ProductCategoryIconMapper
                            .iconForCategory(item.category),
                        contentDescription = "Product Icon",
                        tint = Color.Unspecified
                    )
                }
            }
        }
    }
}


@Preview
@Composable
@ExperimentalMaterial3Api
fun ProductCatalogScreenPreview() {
    ProductCatalogLayout(
        modifier = Modifier,
        uiState = ProductCatalogScreenViewModel.State(),
        onCheckboxClick = { _, _ -> },
        onSearchQuery = {},
        onCategoryClick = {}
    )
}

@Preview(showBackground = true)
@Composable
@ExperimentalMaterial3Api
fun ProductCatalogItemPreview() {

    val sampleProducts = listOf(
        Product(
            name = "Bread",
            category = ProductCategory.HOUSEHOLD
        ),
        Product(
            name = "Milk",
            category = ProductCategory.SPORTS
        ),
        Product(
            name = "Apples",
            category = ProductCategory.OTHER,
            isPurchased = true
        )
    )

    ProductCatalogItem(
        uiState = ProductCatalogScreenViewModel.State(
            products = sampleProducts
        ),
        onCheckboxClick = { _, _ -> }
    )
}

@Preview(showBackground = true)
@Composable
@ExperimentalMaterial3Api
fun CategoryRowPreview() {
    val sampleCategories = listOf(
        ProductCategory.GROCERY,
        ProductCategory.OTHER,
        ProductCategory.SPORTS,
        ProductCategory.CLOTHING,
        ProductCategory.ELECTRONICS,
        ProductCategory.TOYS,
        ProductCategory.HOUSEHOLD
    )

    CategoryRow(
        uiState = ProductCatalogScreenViewModel.State(
            productCategories = sampleCategories,
        ),
        onCategoryClick = {}
    )
}