package com.example.shoply.presentation.screens.productcatalogscreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shoply.domain.model.Product
import com.example.shoply.domain.model.ProductCategory
import com.example.shoply.domain.usecase.product.GetProductUseCase
import com.example.shoply.domain.usecase.product.InsertProductUseCase
import com.example.shoply.presentation.components.dialogs.UiDialog
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID

class ProductCatalogScreenViewModel(
    private val getProductUseCase: GetProductUseCase,
    private val insertProductUseCase: InsertProductUseCase,
) : ViewModel() {
    private val _state = MutableStateFlow(State())
    val state: StateFlow<State> = _state.asStateFlow()

    data class State(
        val allProducts: List<Product> = emptyList(),
        val selectedIds: Set<UUID> = emptySet(),
        val filteredProducts: List<Product> = emptyList(),
        val productCategories: List<ProductCategory> = emptyList(),
        val searchQuery: String = "",
        val selectedCategoryFromDialog: ProductCategory = ProductCategory.ALL,
        val selectedCategoryFromFilterCategory: ProductCategory = ProductCategory.ALL,
        val existingProductCategory: Set<ProductCategory> = emptySet(),
        val activeDialog: UiDialog = UiDialog.NONE,
        val dialogInput: String = "",
        val dialogError: String? = null,
        val userMessage: String? = null,
        val isLastScreenProductListScreen: Boolean? = false
    ) {
        val items: List<Product> =
            if (selectedCategoryFromFilterCategory == ProductCategory.ALL) {
                allProducts
            } else {
                allProducts.filter { it.category == selectedCategoryFromFilterCategory }
            }
    }

    // businnes logic

    fun passSelectedProductList(): List<Product> {
        return _state.value.allProducts
            .filter { _state.value.selectedIds.contains(it.productId) }
    }

    fun onDeleteClick() {

    }

    fun selectProductFromProductsCatalog() {

    }

    fun validateLastScreen(showSpecialIcon: Boolean) {
        _state.update { currentState ->
            currentState.copy(
                isLastScreenProductListScreen = showSpecialIcon
            )
        }
    }


    //dialog operation
    fun onCreateDialog() {
        _state.update { currentState ->
            currentState.copy(activeDialog = UiDialog.INPUT_DIALOG)
        }
    }

    fun onDialogInputChange(input: String) {
        _state.update { currentState ->
            currentState.copy(dialogInput = input, dialogError = null)
        }
    }

    fun onSelectedProductCategory(productCategory: ProductCategory) {
        _state.update { currentState ->
            currentState.copy(selectedCategoryFromDialog = productCategory)
        }
    }

    fun dismissDialog() {
        _state.update { it.copy(activeDialog = UiDialog.NONE) }
    }

    //snackbar
    fun onMessageShown() {
        _state.update { currentState ->
            currentState.copy(
                userMessage = null
            )
        }
    }

    fun onProductCheckedChange(productId: UUID) {
        if (_state.value.selectedIds.contains(productId)) {
            _state.update { currentState ->
                currentState.copy(
                    selectedIds = currentState.selectedIds - setOf(productId)
                )
            }
            return
        }
        _state.update { currentState ->
            currentState.copy(
                selectedIds = currentState.selectedIds + setOf(productId)
            )
        }
    }

    fun addProduct(
        productName: String,
        categoryName: String,
        onSuccess: () -> Unit,
    ) {
        when {
            productNameIsBlank(productName) -> {}
            productNameIsTooLong(productName) -> {}
            productNameAlreadyExists(productName) -> {}
            else -> {
                val category = mapToCategory(categoryName)

                _state.update { currentState ->
                    currentState.copy(
                        allProducts = currentState.allProducts,
                        existingProductCategory =
                            if (!hasCategory(categoryName)) currentState.existingProductCategory + category
                            else currentState.existingProductCategory,
                        userMessage = "Item added successfully"
                    )
                }
                onSuccess()
            }
        }
    }

    fun findProduct(input: String) {
        _state.update { currentState ->
            val filtered =
                if (input.isBlank()) currentState.allProducts else currentState.allProducts
                    .filter { product ->
                        product.name.contains(input, ignoreCase = true)
                    }
            currentState.copy(
                searchQuery = input,
                allProducts = filtered
            )
        }
    }

    fun onCategorySelected(item: ProductCategory) {
        _state.update { currentState ->
            currentState.copy(
                selectedCategoryFromFilterCategory = item
            )
        }
    }

    fun onDialogConfirm() {
        val error = validateProductName(_state.value.dialogInput)
        val errorText = when (error) {
            ProductNameValidationError.BLANK -> "Product name cannot be blank"
            ProductNameValidationError.TOO_LONG -> "Product name cannot be longer than 30 characters"
            ProductNameValidationError.ALREADY_EXISTS -> "Product with this name already exists"
            null -> null
        }

        if (error != null) {
            _state.update {
                it.copy(
                    dialogError = errorText
                )
            }
            return
        }
        addProduct(
            productName = _state.value.dialogInput,
            categoryName = _state.value.selectedCategoryFromDialog.name,

            onSuccess = {
                dismissDialog()
            }
        )
    }

    // init
    init {
        viewModelScope.launch {
            getProductUseCase.invoke().collect {
                val existingProductCategory: Set<ProductCategory> =
                    LinkedHashSet(it.map { p -> p.category } + ProductCategory.ALL)
                _state.update { currentState ->
                    currentState.copy(
                        allProducts = it,
                        existingProductCategory = existingProductCategory,
                        productCategories = ProductCategory.entries
                    )
                }
            }
            insertProductUseCase.invoke()
        }
    }

    // private

    private fun generateProductCategories() {
    }

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

    private fun hasCategory(categoryName: String) = _state.value
        .existingProductCategory.any { it.name == categoryName }
}

enum class ProductNameValidationError {
    BLANK,
    TOO_LONG,
    ALREADY_EXISTS
}