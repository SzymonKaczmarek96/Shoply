package com.example.shoply.presentation.components.dialogs

import androidx.compose.runtime.Composable

@Composable
fun UiDialog.dialogState(
    dialogStateInputDialog: DialogState.InputDialog? = null,
    dialogStateMessageDialog: DialogState.MessageDialog? = null,
): DialogState = when (this) {
    UiDialog.NONE -> DialogState.None
    UiDialog.INPUT_DIALOG -> DialogState.InputDialog(
        title = dialogStateInputDialog?.title ?: "",
        message = dialogStateInputDialog?.message ?: "",
        placeholderFirstInput = dialogStateInputDialog?.placeholderFirstInput ?: "",
        placeholderSecondInput = dialogStateInputDialog?.placeholderSecondInput ?: "",
        confirmButtonText = dialogStateInputDialog?.confirmButtonText ?: "",
        dismissButtonText = dialogStateInputDialog?.dismissButtonText,
        firstInputValue = dialogStateInputDialog?.firstInputValue ?: "",
        secondInputValue = dialogStateInputDialog?.secondInputValue ?: "",
        errorMessage = dialogStateInputDialog?.errorMessage,
        selectedCategory = dialogStateInputDialog?.selectedCategory,
        productCategories = dialogStateInputDialog?.productCategories ?: emptyList(),
        isQuantityRequired = dialogStateInputDialog?.isQuantityRequired
    )

    UiDialog.MESSAGE_DIALOG -> DialogState.MessageDialog(
        title = dialogStateMessageDialog?.title ?: "",
        message = dialogStateMessageDialog?.message ?: "",
        type = DialogType.INFO,
        confirmButtonText = dialogStateMessageDialog?.confirmButtonText ?: "Yes",
        dismissButtonText = dialogStateMessageDialog?.dismissButtonText
    )
}

