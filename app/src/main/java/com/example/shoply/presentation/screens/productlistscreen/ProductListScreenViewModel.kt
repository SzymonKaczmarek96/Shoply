package com.example.shoply.presentation.screens.productlistscreen

import androidx.lifecycle.ViewModel
import com.example.shoply.domain.model.Product
import com.example.shoply.domain.model.ProductCategory
import com.example.shoply.presentation.components.dialogs.UiDialog
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.util.UUID

class ProductListScreenViewModel(

) : ViewModel() {

    private val _state = MutableStateFlow(State())
    val state: StateFlow<State> = _state.asStateFlow()

    data class State(
        val allProducts: List<Product>? = listOf(
            Product(
                name = "Milk",
                category = ProductCategory.OTHER
            ),
            Product(
                name = "Bike",
                category = ProductCategory.SPORTS
            ),
            Product(
                name = "Cacao",
                category = ProductCategory.OTHER
            ),
            Product(
                name = "Ball",
                category = ProductCategory.SPORTS
            ),
            Product(
                name = "Ball",
                category = ProductCategory.SPORTS
            ),
            Product(
                name = "Ball",
                category = ProductCategory.SPORTS
            ),
            Product(
                name = "Ball",
                category = ProductCategory.SPORTS
            ),
            Product(
                name = "Ball",
                category = ProductCategory.SPORTS
            ),
            Product(
                name = "Ball",
                category = ProductCategory.SPORTS
            ),
            Product(
                name = "Ball",
                category = ProductCategory.SPORTS
            ),
            Product(
                name = "Ball",
                category = ProductCategory.SPORTS
            ),
        ),
        val existingCategory: List<ProductCategory>? = listOf(
            ProductCategory.OTHER,
            ProductCategory.SPORTS,
        ),
        val selectedIds: Set<UUID>? = emptySet(),
        val activeDialog: UiDialog = UiDialog.NONE,
        val inputDialog: String? = null,
        val errorMessageDialog: String? = null,
        val productCategories: List<ProductCategory>? = emptyList(),
        val selectedCategoryFromDialog: ProductCategory? = null
    ) {
        val groupedProduct: Map<ProductCategory, List<Product>>? =
            allProducts?.groupBy { it.category }
    }

    //INIT

    init {
        _state.update { currentState ->
            currentState.copy(
                productCategories = ProductCategory.entries
            )
        }
    }

    // BUSINESS LOGIC

    fun updateSelectedIds(productId: UUID) {
        if (_state.value.selectedIds?.contains(productId) == true) {
            _state.update { currentState ->
                currentState.copy(
                    selectedIds = currentState.selectedIds?.minus(setOf(productId)),
                    allProducts = currentState.allProducts?.map { product ->
                        if (product.productId == productId) {
                            product.copy(isPurchased = false)
                        } else {
                            product
                        }
                    }
                )
            }
        } else {
            _state.update { currentState ->
                currentState.copy(
                    selectedIds = currentState.selectedIds?.plus(setOf(productId)),
                    allProducts = currentState.allProducts?.map { product ->
                        if (product.productId == productId) {
                            product.copy(isPurchased = true)
                        } else {
                            product
                        }
                    }
                )
            }
        }
    }

    fun addProduct() {
        val state = _state.value
        val productName = state.inputDialog
        val productCategory = state.selectedCategoryFromDialog


    }

    //DIALOG
    fun onSelectedProductCategory(productCategory: ProductCategory) {
        _state.update { currentState ->
            currentState.copy(selectedCategoryFromDialog = productCategory)
        }
    }

    fun onCreateDialog() {
        _state.update { currentState ->
            currentState.copy(activeDialog = UiDialog.INPUT_DIALOG)
        }
    }

    fun onDismissDialog() {
        _state.update { currentState ->
            currentState.copy(activeDialog = UiDialog.NONE)
        }
    }

    fun onInputChange(input: String) {
        _state.update { currentState ->
            currentState.copy(inputDialog = input)
        }
    }
}