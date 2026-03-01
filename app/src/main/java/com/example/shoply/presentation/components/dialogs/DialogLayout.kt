package com.example.shoply.presentation.components.dialogs

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.shoply.domain.model.ProductCategory
import com.example.shoply.presentation.utils.UiDim

@Composable
fun DialogLayout(
    dialogState: DialogState,
    modifier: Modifier,
    onDismiss: () -> Unit,
    onValueChange: ((String) -> Unit)? = null,
    onCategorySelected: ((ProductCategory) -> Unit)? = null,
    onConfirm: (() -> Unit)? = null
) {
    DialogContent(
        dialogState = dialogState,
        modifier = modifier,
        onDismiss = onDismiss,
        onValueChange = onValueChange,
        onCategorySelected = onCategorySelected,
        onConfirm = onConfirm
    )
}

@Composable
fun DialogContent(
    dialogState: DialogState,
    modifier: Modifier = Modifier,
    onDismiss: () -> Unit,
    onValueChange: ((String) -> Unit)? = null,
    onCategorySelected: ((ProductCategory) -> Unit)? = null,
    onConfirm: (() -> Unit)? = null
) {
    when (dialogState) {

        DialogState.None -> Unit

        is DialogState.MessageDialog -> {
            AlertDialog(
                modifier = modifier,
                icon = {
                    Icon(
                        imageVector = dialogState.type.toIcon(),
                        contentDescription = null
                    )
                },
                onDismissRequest = onDismiss,
                title = { Text(dialogState.title) },
                text = { Text(dialogState.message) },
                confirmButton = {
                    TextButton(
                        onClick = {
                            onDismiss()
                        }
                    ) {
                        Text(dialogState.confirmButtonText)
                    }
                },
                dismissButton = dialogState.dismissButtonText?.let {
                    {
                        TextButton(onClick = onDismiss) {
                            Text(it)
                        }
                    }
                }
            )
        }
        is DialogState.InputDialog -> {
            InputDialogContent(
                state = dialogState,
                onDismiss = onDismiss,
                onValueChange = { onValueChange?.invoke(it) },
                onCategorySelected = { onCategorySelected?.invoke(it) },
                onConfirm = { onConfirm?.invoke() }
            )
        }
    }
}


@Composable
private fun InputDialogContent(
    state: DialogState.InputDialog,
    onDismiss: () -> Unit,
    onValueChange: ((String) -> Unit),
    onCategorySelected: ((ProductCategory) -> Unit),
    onConfirm: (() -> Unit)
) {
    AlertDialog(
        onDismissRequest = { onDismiss() },
        title = { Text(state.title) },
        text = {
            Column {
                Text(
                    text = state.message
                )
                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = UiDim.PADDING_MEDIUM)
                        .background(
                            color = Color(0xFFF9FAFB),
                            shape = RoundedCornerShape(12.dp)
                        ),
                    value = state.firstInputValue,
                    maxLines = 1,
                    onValueChange = { onValueChange.invoke(it) },
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent
                    ),
                    placeholder = {
                        Text("Enter product name")
                    }
                )
                Spacer(modifier = Modifier.padding(UiDim.PADDING_MEDIUM))

                Text(
                    text = "Add category: "
                )
                DropdownMenuProductCategories(
                    categories = state.productCategories,
                    selectedCategory = state.selectedCategory,
                    onCategorySelected = { onCategorySelected.invoke(it) }
                )

                state.errorMessage?.let {
                    Text(
                        text = it,
                        color = Color.Red,
                        modifier = Modifier.padding(top = UiDim.PADDING_LARGE)
                    )
                }

            }
        },

        confirmButton = {
            TextButton(onClick = { onConfirm() }) {
                Text(state.confirmButtonText)
            }
        },
        dismissButton = state.dismissButtonText?.let {
            {
                TextButton(onClick = { onDismiss() }) {
                    Text(it)
                }
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DropdownMenuProductCategories(
    categories: List<ProductCategory>,
    selectedCategory: ProductCategory?,
    onCategorySelected: (ProductCategory) -> Unit
) {

    var expanded by remember { mutableStateOf(false) }

    Box() {
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded }
        ) {
            OutlinedTextField(
                readOnly = true,
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth()
                    .padding(top = UiDim.PADDING_MEDIUM)
                    .background(
                        color = Color(0xFFF9FAFB),
                        shape = RoundedCornerShape(12.dp)
                    ),
                value = selectedCategory?.name ?: "",
                onValueChange = {},
                maxLines = 1,
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                placeholder = {
                    Text("Choose category")
                },
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                }
            )
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                categories.forEach { category ->
                    DropdownMenuItem(
                        text = { Text(category.name) },
                        onClick = {
                            onCategorySelected(category)
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}

private fun DialogType.toIcon(): ImageVector =
    when (this) {
        DialogType.INFO -> Icons.Default.Info
        DialogType.SUCCESS -> Icons.Default.CheckCircle
        DialogType.ERROR -> Icons.Default.Error
    }

@Preview
@Composable
private fun AlertDialogLayoutPreview() {
    DialogContent(
        dialogState = DialogState.MessageDialog(
            title = "Error",
            message = "An error occurred while processing your request.",
            type = DialogType.INFO,
            confirmButtonText = "OK",
        ),
        modifier = Modifier,
        onDismiss = {},

    )
}

@Preview
@Composable
private fun InputDialogLayoutPreview() {
    InputDialogContent(
        state = DialogState.InputDialog(
            title = "Add Product",
            message = "Please enter your product: ",
            confirmButtonText = "Submit",
            dismissButtonText = "Cancel",
            errorMessage = "Product name cannot be empty",
        ),
        onDismiss = {},
        onValueChange = {},
        onCategorySelected = { },
        onConfirm = {}
    )
}

