package com.example.shoply.presentation.screens.productlistscreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shoply.domain.model.Product
import com.example.shoply.domain.model.ProductCategory
import com.example.shoply.domain.model.ProductInList
import com.example.shoply.domain.usecase.productinlist.AddProductInListUseCase
import com.example.shoply.domain.usecase.productinlist.GetProductInListUseCase
import com.example.shoply.domain.usecase.productlist.AddProductListUseCase
import com.example.shoply.presentation.components.dialogs.UiDialog
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID

class ProductListScreenViewModel(
    private val addProductListUseCase: AddProductListUseCase,
    private val getProductInList: GetProductInListUseCase,
    private val addProductInListUseCase: AddProductInListUseCase,
) : ViewModel() {

    private var observeJob: Job? = null
    private val _state = MutableStateFlow(State())
    val state: StateFlow<State> = _state.asStateFlow()

    data class State(
        val listId: UUID? = null,
        val allProducts: List<ProductInList>? = emptyList(),
        val existingCategory: List<ProductCategory>? = listOf(
            ProductCategory.OTHER,
            ProductCategory.SPORTS,
        ),
        val selectedIds: Set<UUID>? = emptySet(),
        val activeDialog: UiDialog = UiDialog.NONE,
        val inputDialog: String? = null,
        val errorMessageDialog: String? = null,
        val productCategories: List<ProductCategory>? = emptyList(),
        val selectedCategoryFromDialog: ProductCategory? = null,
    ) {
        val groupedProduct: Map<ProductCategory, List<ProductInList>>? =
            allProducts?.groupBy { it.product.category }
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
    fun updateListId(listId: UUID?) {
        if (_state.value.listId == listId) return

        _state.update { currentState ->
            currentState.copy(
                listId = listId
            )
        }

        observeProducts(listId)
    }

    private fun observeProducts(listId: UUID?) {
        if (listId == null) return
        observeJob?.cancel()
        observeJob = viewModelScope.launch {
            getProductInList(listId).collect { products ->
                _state.update {
                    it.copy(
                        allProducts = products,
                    )
                }
            }
        }
    }

    fun addProductInList(
        productListId: UUID,
        product: Product
    ) {
        viewModelScope.launch {
            addProductInListUseCase.invoke(
                ProductInList(
                    productListId = productListId,
                    product = product,
                )
            )
        }
    }

    fun updateSelectedIds(productId: UUID) {
        if (_state.value.selectedIds?.contains(productId) == true) {
            _state.update { currentState ->
                currentState.copy(
                    selectedIds = currentState.selectedIds?.minus(setOf(productId)),
                    allProducts = currentState.allProducts?.map { product ->
                        if (product.id == productId) {
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
                        if (product.id == productId) {
                            product.copy(isPurchased = true)
                        } else {
                            product
                        }
                    }
                )
            }
        }
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