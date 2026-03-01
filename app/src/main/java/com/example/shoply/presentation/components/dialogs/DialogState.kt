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
        val selectedCategory: ProductCategory? = null,
        val dismissButtonText: String? = null,
        val errorMessage: String? = null,
        val productCategories: List<ProductCategory> = emptyList(),
    ) : DialogState
}

enum class DialogType {
    ERROR,
    SUCCESS,
    INFO
}



