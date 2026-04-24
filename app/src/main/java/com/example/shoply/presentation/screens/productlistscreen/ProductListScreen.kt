package com.example.shoply.presentation.screens.productlistscreen

import FabConfig
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.shoply.domain.model.Product
import com.example.shoply.domain.model.ProductCategory
import com.example.shoply.presentation.components.dialogs.DialogLayout
import com.example.shoply.presentation.components.dialogs.DialogState
import com.example.shoply.presentation.components.dialogs.toDialogInputState
import com.example.shoply.presentation.mapper.ProductCategoryIconMapper
import com.example.shoply.presentation.utils.UiDim
import org.koin.compose.koinInject
import java.util.UUID

@Composable
@ExperimentalMaterial3Api
fun ProductListScreen(
    viewModel: ProductListScreenViewModel = koinInject(),
    listId: UUID?,
    onFabClickChange: (FabConfig) -> Unit,
) {
    val uiState by viewModel.state.collectAsStateWithLifecycle()

    val dialogState = uiState.activeDialog.toDialogInputState(
        uiState = DialogState.InputDialog(
            title = "Add Product",
            message = "Enter the product name and select a category",
            placeholderFirstInput = "Enter product name",
            confirmButtonText = "Add",
            dismissButtonText = "Cancel",
            firstInputValue = uiState.inputDialog ?: "",
            errorMessage = uiState.errorMessageDialog,
            selectedCategory = uiState.selectedCategoryFromDialog,
            productCategories = uiState.productCategories ?: emptyList()
        )
    )
    LaunchedEffect(Unit) {
        onFabClickChange.invoke(
            FabConfig(
                visible = true,
                onClick = {
                    viewModel.onCreateDialog()
                }
            )
        )
    }

    RootView(
        modifier = Modifier,
        uiState = uiState,
        onCheckboxClick = { uuid, boolean ->
            viewModel.updateSelectedIds(uuid)
        }
    )

    DialogLayout(
        dialogState = dialogState,
        modifier = Modifier,
        onDismiss = {
            viewModel.onDismissDialog()
        },
        onValueChange = {
            viewModel.onInputChange(it)
        },
        onCategorySelected = {
            viewModel.onSelectedProductCategory(it)
        },
        onConfirm = {
            viewModel.addProduct()
        }
    )
}


@Composable
@ExperimentalMaterial3Api
private fun RootView(
    modifier: Modifier,
    uiState: ProductListScreenViewModel.State,
    onCheckboxClick: (UUID, Boolean) -> Unit = { _, _ -> },
    onSearchQuery: (String) -> Unit = {},
) {
    ProductListLayout(
        modifier = modifier,
        uiState = uiState,
        onCheckboxClick = onCheckboxClick,
        onSearchQuery = onSearchQuery
    )
}

@Composable
@ExperimentalMaterial3Api
private fun ProductListLayout(
    modifier: Modifier,
    uiState: ProductListScreenViewModel.State,
    onCheckboxClick: (UUID, Boolean) -> Unit = { _, _ -> },
    onSearchQuery: (String) -> Unit = {},
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
                    text = "Shopping List",
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
            ProductCatalogItem(
                uiState = uiState,
                onCheckboxClick = onCheckboxClick
            )
        }
    }
}

@Composable
@ExperimentalMaterial3Api
private fun ProductCatalogItem(
    uiState: ProductListScreenViewModel.State,
    onCheckboxClick: (UUID, Boolean) -> Unit = { _, _ -> },
) {
    LazyColumn {
        uiState.groupedProduct?.forEach { (category, product) ->
            item(key = category.name) {
                CategoryHeader(category)
            }
            items(
                items = product,
            ) { item ->

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = UiDim.PADDING_SMALL)
                        .background(
                            color = Color(0xFFF5F5F5),
                            shape = RoundedCornerShape(8.dp)
                        ),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {

                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Checkbox(
                            checked = uiState.selectedIds?.contains(item.productId) == true,
                            onCheckedChange = {
                                onCheckboxClick.invoke(
                                    item.productId, it
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
                            textDecoration = if (item.isPurchased) TextDecoration.LineThrough else null,
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
}

@Composable
private fun CategoryHeader(
    productCategory: ProductCategory
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                vertical = UiDim.PADDING_MEDIUM,
            )
            .background(
                color = Color(0xFFF5F5F5),
                shape = RoundedCornerShape(8.dp)
            )
            .padding(UiDim.PADDING_MEDIUM),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        Icon(
            modifier = Modifier.size(UiDim.PADDING_LARGE),
            imageVector = ProductCategoryIconMapper.iconForCategory(productCategory),
            contentDescription = productCategory.name,
            tint = Color(0xff4B5563)
        )
        Text(
            modifier = Modifier.padding(start = UiDim.PADDING_MEDIUM),
            text = productCategory.name,
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color(0xff4B5563)
        )
    }
}

@Preview
@Composable
@ExperimentalMaterial3Api
fun ProductCatalogScreenPreview() {
    ProductListLayout(
        modifier = Modifier,
        uiState = ProductListScreenViewModel.State(),
        onCheckboxClick = { _, _ -> },
        onSearchQuery = {},
    )
}

@Preview
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
        ),
    )
}