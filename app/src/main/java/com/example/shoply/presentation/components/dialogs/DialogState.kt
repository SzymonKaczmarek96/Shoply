package com.example.shoply.presentation.components.dialogs

import com.example.shoply.domain.model.ProductCategory


sealed interface DialogState {

    object None : DialogState

    data class MessageDialog(
        val title: String,
        val message: String,
        val type: DialogType = DialogType.INFO,
        val confirmButtonText: String,
        val dismissButtonText: String? = null,
    ) : DialogState

    data class InputDialog(
        val title: String,
        val message: String,
        val confirmButtonText: String,
        val firstInputValue: String = "",
        val secondInputValue: String = "",
        val placeholderFirstInput: String? = null,
        val placeholderSecondInput: String? = null,
        val selectedCategory: ProductCategory? = null,
        val dismissButtonText: String? = null,
        val errorMessage: String? = null,
        val isQuantityRequired: Boolean? = false,
        val productCategories: List<ProductCategory> = emptyList(),
    ) : DialogState
}

enum class UiDialog {
    NONE, INPUT_DIALOG, MESSAGE_DIALOG
}

enum class DialogType {
    ERROR,
    SUCCESS,
    INFO
}





