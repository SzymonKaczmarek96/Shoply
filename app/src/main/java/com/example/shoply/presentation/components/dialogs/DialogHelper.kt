package com.example.shoply.presentation.components.dialogs

import androidx.compose.runtime.Composable

@Composable
fun UiDialog.toDialogInputState(
    uiState: DialogState.InputDialog,
): DialogState = when (this) {
    UiDialog.NONE -> DialogState.None
    UiDialog.INPUT_DIALOG -> DialogState.InputDialog(
        title = uiState.title,
        message = uiState.message,
        placeholderFirstInput = uiState.placeholderFirstInput,
        confirmButtonText = uiState.confirmButtonText,
        dismissButtonText = uiState.dismissButtonText,
        firstInputValue = uiState.firstInputValue,
        errorMessage = uiState.errorMessage,
        selectedCategory = uiState.selectedCategory,
        productCategories = uiState.productCategories
    )
}
