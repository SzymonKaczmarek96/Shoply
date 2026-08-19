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
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.PlaylistAddCheckCircle
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.shoply.domain.model.Product
import com.example.shoply.domain.model.ProductCategory
import com.example.shoply.presentation.components.AppDropdownItem
import com.example.shoply.presentation.components.DropdownMenuConfig
import com.example.shoply.presentation.components.dialogs.DialogLayout
import com.example.shoply.presentation.components.dialogs.DialogState
import com.example.shoply.presentation.components.dialogs.dialogState
import com.example.shoply.presentation.components.snackbar.SnackbarManager
import com.example.shoply.presentation.mapper.ProductCategoryIconMapper
import com.example.shoply.presentation.utils.UiDim
import org.koin.androidx.compose.koinViewModel
import java.util.UUID

@Composable
@ExperimentalMaterial3Api
fun ProductCatalogScreen(
    modifier: Modifier,
    viewModel: ProductCatalogScreenViewModel = koinViewModel(),
    onFabConfigChange: (FabConfig) -> Unit,
    onNavigateBack: () -> Unit,
    showSpecialIcon: Boolean,
    onMenuConfigChange: (DropdownMenuConfig) -> Unit,
    listId: UUID?,
) {
    val uiState by viewModel.state.collectAsStateWithLifecycle()

    val inputDialogState = uiState.activeDialog.dialogState(
        dialogStateInputDialog = DialogState.InputDialog(
            title = if (uiState.isUpdateDialog) "Update Product" else "Add Product",
            message = "Enter the product name and select a category",
            placeholderFirstInput = "Enter product name",
            confirmButtonText = if (uiState.isUpdateDialog) "Update" else "Add",
            dismissButtonText = "Cancel",
            firstInputValue = uiState.dialogInput,
            errorMessage = uiState.dialogError,
            selectedCategory = uiState.selectedCategoryFromDialog,
            productCategories = uiState.productCategories
        )
    )

    val updateCategoryDialog = uiState.activeDialog.dialogState(
        dialogStateInputDialog = DialogState.InputDialog(
            title = "Update Category",
            message = "Choose a new category for the selected products",
            confirmButtonText = "Update",
            dismissButtonText = "Cancel",
            selectedCategory = uiState.selectedCategoryFromDialog,
            productCategories = uiState.productCategories
        )
    )

    DisposableEffect(Unit) {
        onMenuConfigChange(
            DropdownMenuConfig(
                dropdownItems = listOf(
                    AppDropdownItem(
                        text = "Add to Favorites",
                        onClick = {
                            viewModel.changeCategoryToFavorite()
                        },
                        icon = Icons.Default.Favorite
                    ),
                    AppDropdownItem(
                        text = "Change Category",
                        onClick = {
                            viewModel.showUpdateCategoryDialog()
                        },
                        icon = Icons.Default.Edit
                    )
                )
            )
        )
        onDispose {
            onMenuConfigChange(DropdownMenuConfig())
        }
    }

    LaunchedEffect(Unit) {
        onFabConfigChange(
            FabConfig(
                visible = true,
                onClick = {
                    viewModel.setDialogTypeOnUpdate(false)
                    viewModel.onCreateDialog()
                }
            )
        )
    }

    LaunchedEffect(showSpecialIcon) {
        viewModel.setIsLastScreen(showSpecialIcon)
    }

    LaunchedEffect(uiState.userMessage) {
        uiState.userMessage?.let {
            SnackbarManager.showSnackbar(
                snackbarEvent = SnackbarManager.SnackbarEvent(
                    message = it,
                    type = if (!uiState.isError) SnackbarManager.SnackbarType.SUCCESS else
                        SnackbarManager.SnackbarType.ERROR,
                    duration = SnackbarDuration.Short,
                    withDismissAction = true,
                )
            )
        }
    }

    RootView(
        modifier = modifier,
        uiState = uiState,
        onCheckboxClick = { productId ->
            viewModel.onProductCheckedChange(productId)
        },
        onSearchQuery = { viewModel.findProduct(it) },
        onCategoryClick = {
            viewModel.onCategorySelected(it)
        },
        onSelectedItemClick = {
            viewModel.addSelectedProductList(listId ?: UUID.fromString(""))
            onNavigateBack()
        },
        onDeleteIconClick = {
            viewModel.deleteProducts()
        },
        onEditClick = { productId ->
            viewModel.selectProduct(productId)
            viewModel.setDialogTypeOnUpdate(true)
            viewModel.onCreateDialog()
        }
    )

    DialogLayout(
        dialogState = if (uiState.isCategoryUpdate) updateCategoryDialog else inputDialogState,
        modifier = Modifier,
        onDismiss = { viewModel.dismissDialog() },
        onValueChange = { viewModel.onDialogInputChange(it) },
        onCategorySelected = viewModel::onSelectedProductCategory,
        onConfirm = {
            if (uiState.isCategoryUpdate)
                viewModel.changeCategoryForSelectedCategory()
            else
                viewModel.confirmInput()
        }
    )
}

@Composable
@ExperimentalMaterial3Api
private fun RootView(
    modifier: Modifier,
    uiState: ProductCatalogScreenViewModel.State,
    onCheckboxClick: (UUID) -> Unit,
    onSearchQuery: (String) -> Unit,
    onCategoryClick: (ProductCategory) -> Unit,
    onSelectedItemClick: () -> Unit,
    onDeleteIconClick: () -> Unit,
    onEditClick: (UUID) -> Unit,
) {
    ProductCatalogLayout(
        modifier = modifier,
        uiState = uiState,
        onCheckboxClick = onCheckboxClick,
        onSearchQuery = onSearchQuery,
        onCategoryClick = onCategoryClick,
        onSelectedItemClick = onSelectedItemClick,
        onDeleteIconClick = onDeleteIconClick,
        onEditClick = onEditClick
    )
}

@Composable
@ExperimentalMaterial3Api
private fun ProductCatalogLayout(
    modifier: Modifier,
    uiState: ProductCatalogScreenViewModel.State,
    onCheckboxClick: (UUID) -> Unit,
    onSearchQuery: (String) -> Unit,
    onCategoryClick: (ProductCategory) -> Unit,
    onSelectedItemClick: () -> Unit,
    onDeleteIconClick: () -> Unit,
    onEditClick: (UUID) -> Unit,
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
                    if (uiState.selectedIds.isNotEmpty() && uiState.isLastScreenProductListScreen == true) {
                        uiState.selectedIds.any().let {
                            IconButton(
                                modifier = Modifier
                                    .size(UiDim.ICON_SIZE),
                                onClick = {
                                    onSelectedItemClick()
                                },
                                colors = IconButtonDefaults.iconButtonColors(
                                    disabledContainerColor = Color.White,
                                    disabledContentColor = Color.White
                                ),
                            ) {
                                Icon(
                                    modifier = Modifier,
                                    imageVector = Icons.Default.PlaylistAddCheckCircle,
                                    contentDescription = "Selected icon",
                                    tint = Color(0xff4B5563)
                                )

                            }
                        }
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
                        onClick = {
                            onDeleteIconClick.invoke()
                        }
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
                onCheckboxClick = onCheckboxClick,
                onEditClick = onEditClick
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
        val categories = uiState.existingProductCategory.sorted()
        items(categories) { item ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .padding(end = UiDim.PADDING_MEDIUM)
                    .clickable {
                        onCategoryClick.invoke(item)
                    }
                    .background(
                        color = if (uiState.selectedCategoryFromFilterCategory.name == item.name)
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
                    tint = if (uiState.selectedCategoryFromFilterCategory.name == item.name)
                        Color(0xFFFFFFFF) else Color(0xFF8F8F8F),
                )
                Text(
                    text = item.name.lowercase().replaceFirstChar { it.uppercase() },
                    modifier = Modifier
                        .padding(end = UiDim.PADDING_LARGE),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = if (uiState.selectedCategoryFromFilterCategory.name == item.name)
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
    onCheckboxClick: (UUID) -> Unit,
    onEditClick: (UUID) -> Unit,
) {
    LazyColumn {
        items(items = uiState.items) { item ->
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
                        checked = uiState.selectedIds.contains(item.productId),
                        onCheckedChange = {
                            onCheckboxClick.invoke(
                                item.productId
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
                        fontWeight = FontWeight.Normal,
                        color = Color(0xff111827)
                    )
                }
                Row(

                ) {
                    Icon(
                        modifier = Modifier
                            .padding(horizontal = UiDim.PADDING_LARGE)
                            .clickable(
                                true,
                                onClick = {
                                    onEditClick.invoke(
                                        item.productId
                                    )
                                }
                            ),
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Edit",
                        tint = Color.Unspecified
                    )


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
        onCheckboxClick = { _ -> },
        onSearchQuery = {},
        onCategoryClick = {},
        onSelectedItemClick = {},
        onDeleteIconClick = {},
        onEditClick = {},
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
        )
    )

    ProductCatalogItem(
        uiState = ProductCatalogScreenViewModel.State(
            allProducts = sampleProducts
        ),
        onCheckboxClick = { _ -> },
        onEditClick = { _ -> }
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