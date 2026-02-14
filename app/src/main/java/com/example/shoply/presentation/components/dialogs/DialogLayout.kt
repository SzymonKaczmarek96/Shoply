package com.example.shoply.presentation.components.dialogs

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.shoply.presentation.utils.UiDim

@Composable
fun DialogLayout(
    dialogState: DialogState,
    modifier: Modifier,
) {

    DialogContent(
        state = dialogState,
        modifier = modifier,
    )
}

@Composable
private fun DialogContent(
    state: DialogState,
    modifier: Modifier,
) {
    when (state) {
        is DialogState.None -> {}
        is DialogState.MessageDialog -> {
            AlertDialog(
                modifier = modifier,
                icon = {
                    Icon(
                        imageVector = state.type.toIcon(),
                        contentDescription = null
                    )
                },
                onDismissRequest = { state.onDismiss?.invoke() },
                title = { Text(state.title) },
                text = { Text(state.message) },
                confirmButton = {
                    TextButton(onClick = { state.onConfirm.invoke() }) {
                        Text(state.confirmButtonText)
                    }
                },
                dismissButton = state.dismissButtonText?.let {
                    {
                        TextButton(onClick = { state.onDismiss?.invoke() }) {
                            Text(it)
                        }
                    }
                }
            )
        }

        is DialogState.InputDialog -> {
            InputDialogContent(
                state = state,
                modifier = modifier
            )
        }
    }
}


@Composable
private fun InputDialogContent(
    state: DialogState.InputDialog,
    modifier: Modifier
) {
    var input by rememberSaveable { mutableStateOf("") }
    var categoryInput by rememberSaveable { mutableStateOf("") }


    AlertDialog(
        modifier = modifier,
        onDismissRequest = { state.onDismiss?.invoke() },
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
                    value = input,
                    maxLines = 1,
                    onValueChange = { input = it },
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
                    state = state,
                    categoryInput = categoryInput,
                    onCategoryChange = { categoryInput = it })
            }
        },
        confirmButton = {
            TextButton(onClick = { state.onConfirm.invoke(input, categoryInput) }) {
                Text(state.confirmButtonText)
            }
        },
        dismissButton = state.dismissButtonText?.let {
            {
                TextButton(onClick = { state.onDismiss?.invoke() }) {
                    Text(it)
                }
            }
        }
    )
}

//FIXME dropdown menu doesn't work, cancel button doesn't work
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DropdownMenuProductCategories(
    state: DialogState.InputDialog,
    categoryInput: String,
    onCategoryChange: (String) -> Unit
) {
    val context = LocalContext.current

    var expanded by remember { mutableStateOf(false) }
    var selectedText by remember {
        mutableStateOf(state.productCategories?.getOrNull(0))
    }
    Box() {
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = {
                expanded = !expanded
            }
        ) {
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = UiDim.PADDING_MEDIUM)
                    .background(
                        color = Color(0xFFF9FAFB),
                        shape = RoundedCornerShape(12.dp)
                    ),
                value = categoryInput,
                maxLines = 1,
                onValueChange = { onCategoryChange(it) },
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
                    IconButton(
                        onClick = {

                        },
                        content = {
                            Icon(
                                imageVector = Icons.Default.ArrowDropDown,
                                contentDescription = null
                            )
                        }
                    )
                }
            )
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                state.productCategories?.forEach { item ->
                    DropdownMenuItem(
                        text = { Text(text = item.name) },
                        onClick = {
                            selectedText = item
                            expanded = false
                            Toast.makeText(context, item.name, Toast.LENGTH_SHORT).show()
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
        state = DialogState.MessageDialog(
            title = "Error",
            message = "An error occurred while processing your request.",
            type = DialogType.INFO,
            confirmButtonText = "OK",
            onConfirm = { },
        ),
        modifier = Modifier
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
            onConfirm = { _, _ -> },
            onDismiss = {}
        ),
        modifier = Modifier
    )
}

