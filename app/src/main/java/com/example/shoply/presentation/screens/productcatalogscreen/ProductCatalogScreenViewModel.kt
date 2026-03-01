package com.example.shoply.presentation.screens.productcatalogscreen

import androidx.lifecycle.ViewModel
import com.example.shoply.domain.model.Product
import com.example.shoply.domain.model.ProductCategory
import com.example.shoply.presentation.components.dialogs.DialogState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.util.UUID

class ProductCatalogScreenViewModel(
    private val testUseCase: TestUseCase
) : ViewModel() {
    // business logic
    private val _state = MutableStateFlow(State())
    val state: StateFlow<State> = _state.asStateFlow()

    data class State(
        val allProducts: List<Product> = emptyList(),
        val products: List<Product> = emptyList(),
        val productCategories: List<ProductCategory> = emptyList(),
        val searchQuery: String = "",
        val dialog: DialogState = DialogState.None,
        val selectedCategory: ProductCategory = ProductCategory.ALL,
        val previousSelectedCategory: ProductCategory? = null
    )

    fun onProductCheckedChange(productId: UUID, isChecked: Boolean) {
        val updatedProducts = _state.value.products.map {
            if (it.uuid == productId) {
                it.copy(isSelected = isChecked)
            } else {
                it
            }
        }
        _state.update { currentState ->
            currentState.copy(
                products = updatedProducts,
                allProducts = updatedProducts
            )
        }
    }

    fun addProduct(
        productName: String,
        categoryName: String,
        onSuccess: () -> Unit,
    ) {
        when {
            productNameIsBlank(productName) -> _state.value.dialog
            productNameIsTooLong(productName) -> {}
            productNameAlreadyExists(productName) -> {}
            else -> {
                _state.update { currentState ->
                    currentState.copy(
                        allProducts = _state.value.allProducts + listOf(
                            Product(
                                name = productName,
                                category = mapToCategory(categoryName)
                            )
                        ),
                        products = _state.value.products + listOf(
                            Product(
                                name = productName,
                                category = mapToCategory(categoryName)
                            )
                        ),
                    )
                }
                onSuccess.invoke()
            }
        }
    }

    fun findProduct(input: String) {
        _state.update { currentState ->
            val filtered =
                if (input.isBlank()) currentState.allProducts else currentState.products.filter {
                    it.name.contains(input, ignoreCase = true)
                }
            currentState.copy(
                searchQuery = input,
                products = filtered
            )
        }
    }

    fun onCategorySelected(item: ProductCategory) {
        _state.update { currentState ->
            if (item == ProductCategory.ALL) {
                restartProductListWithSelectedItems()
                currentState.copy(
                    selectedCategory = item,
                    products = currentState.allProducts
                )
            } else {
                if (currentState.selectedCategory != ProductCategory.ALL) {
                    restartProductListWithSelectedItems()
                }
                currentState.copy(
                    selectedCategory = item,
                    products = currentState.products.filter { it.category == item }
                )
            }
        }
    }

//dialog

    fun openAddProductDialog() {
        _state.update { currentState ->
            currentState.copy(
                dialog = DialogState.InputDialog(
                    title = "Add Product",
                    message = "Enter the product name and select a category",
                    confirmButtonText = "Add",
                    dismissButtonText = "Cancel",
                    productCategories = currentState.productCategories
                )
            )
        }
    }

    fun dismissDialog() {
        _state.update { it.copy(dialog = DialogState.None) }
    }

    fun onDialogNameChange(input: String) {
        val dialog = state.value.dialog as? DialogState.InputDialog ?: return
        _state.update {
            it.copy(
                dialog = dialog.copy(
                    firstInputValue = input,
                    errorMessage = null
                )
            )
        }
    }

    fun onDialogCategorySelected(category: ProductCategory) {
        val dialog = state.value.dialog as? DialogState.InputDialog ?: return
        _state.update {
            it.copy(
                dialog = dialog.copy(
                    selectedCategory = category
                )
            )
        }
    }

    fun onDialogConfirm() {
        val dialog = state.value.dialog as? DialogState.InputDialog ?: return
        val error = validateProductName(dialog.firstInputValue)
        val errorText = when (error) {
            ProductNameValidationError.BLANK -> "Product name cannot be blank"
            ProductNameValidationError.TOO_LONG -> "Product name cannot be longer than 30 characters"
            ProductNameValidationError.ALREADY_EXISTS -> "Product with this name already exists"
            null -> null
        }

        if (error != null) {
            _state.update {
                it.copy(
                    dialog = dialog.copy(errorMessage = errorText)
                )
            }
            return
        }

        addProduct(
            productName = dialog.firstInputValue,
            categoryName = dialog.selectedCategory?.name
                ?: ProductCategory.OTHER.name,
            onSuccess = {}
        )

        dismissDialog()
    }

    // init
    init {
        _state.value = state.value.copy(
            allProducts = testUseCase.products,
            products = testUseCase.products,
            productCategories = listOf(
                ProductCategory.ALL,
                ProductCategory.GROCERY,
                ProductCategory.OTHER,
                ProductCategory.SPORTS,
                ProductCategory.CLOTHING,
                ProductCategory.ELECTRONICS,
                ProductCategory.TOYS,
                ProductCategory.HOUSEHOLD
            )
        )
    }

    // private

    private fun validateProductName(name: String): ProductNameValidationError? {
        return when {
            name.isBlank() -> ProductNameValidationError.BLANK
            name.length >= 30 -> ProductNameValidationError.TOO_LONG
            state.value.allProducts.any {
                it.name.equals(name, ignoreCase = true)
            } -> ProductNameValidationError.ALREADY_EXISTS

            else -> null
        }
    }

    private fun productNameIsBlank(productName: String) = productName.isBlank()

    private fun productNameIsTooLong(productName: String) = productName.length >= 30

    private fun productNameAlreadyExists(productName: String) =
        state.value.allProducts.any { it.name.lowercase() == productName.lowercase() }

    private fun mapToCategory(value: String): ProductCategory {
        return ProductCategory.entries.first { it.name == value }
    }

    private fun restartProductListWithSelectedItems() {
        _state.update { currentState ->
            val selectedProducts = currentState.products.filter { it.isSelected }
            currentState.copy(products = _state.value.allProducts.map { product ->
                if (product.uuid in selectedProducts.map { it.uuid }) {
                    product.copy(isSelected = true)
                } else {
                    product.copy(isSelected = false)
                }
            })
        }
    }

}

enum class ProductNameValidationError {
    BLANK,
    TOO_LONG,
    ALREADY_EXISTS
}