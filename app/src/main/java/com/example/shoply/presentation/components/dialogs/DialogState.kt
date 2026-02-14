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
        val onConfirm: () -> Unit,
        val onDismiss: (() -> Unit)? = null,
    ) : DialogState

    data class InputDialog(
        val title: String,
        val message: String,
        val confirmButtonText: String,
        val firstInputValue: String = "",
        val secondInputValue: String = "",
        val dismissButtonText: String? = null,
        val onConfirm: (String, String) -> Unit,
        val onDismiss: (() -> Unit)? = null,
        val productCategories: List<ProductCategory>? = emptyList(),
        val onCategorySelectClick: ((Boolean) -> Unit)? = null
    ) : DialogState

}


enum class DialogType {
    ERROR,
    SUCCESS,
    INFO
}

