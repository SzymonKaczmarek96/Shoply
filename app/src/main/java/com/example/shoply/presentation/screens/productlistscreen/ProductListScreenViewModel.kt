package com.example.shoply.presentation.screens.productlistscreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shoply.domain.model.Product
import com.example.shoply.domain.model.ProductCategory
import com.example.shoply.domain.model.ProductInList
import com.example.shoply.domain.usecase.UseCaseResult
import com.example.shoply.domain.usecase.product.GetOrCreateProductUseCase
import com.example.shoply.domain.usecase.productinlist.AddProductInListUseCase
import com.example.shoply.domain.usecase.productinlist.DeleteProductInListUseCase
import com.example.shoply.domain.usecase.productinlist.DeleteProductsInListUseCase
import com.example.shoply.domain.usecase.productinlist.FindProductsInListUseCase
import com.example.shoply.domain.usecase.productinlist.GetProductInListUseCase
import com.example.shoply.domain.usecase.productinlist.UpdateProductInListUseCase
import com.example.shoply.domain.usecase.productlist.GetProductListsWithDetailsUseCase
import com.example.shoply.presentation.components.dialogs.UiDialog
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.scan
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID

class ProductListScreenViewModel(
    private val getOrCreateProductUseCase: GetOrCreateProductUseCase,
    private val getProductInList: GetProductInListUseCase,
    private val addProductInListUseCase: AddProductInListUseCase,
    private val deleteProductInListUseCase: DeleteProductInListUseCase,
    private val updateProductInListUseCase: UpdateProductInListUseCase,
    private val getProductListsWithDetailsUseCase: GetProductListsWithDetailsUseCase,
    private val deleteProductsInListUseCase: DeleteProductsInListUseCase,
    private val findProductsInListUseCase: FindProductsInListUseCase,
) : ViewModel() {

    private var observeJob: Job? = null
    private var searchJob: Job? = null

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
        val dialogInput: String? = null,
        val quantityInputDialog: String? = "1",
        val errorMessageDialog: String? = null,
        val productCategories: List<ProductCategory>? = emptyList(),
        val selectedCategoryFromDialog: ProductCategory? = null,
        val userMessage: String? = null,
        val isInputDialogState: Boolean = true,
        val foundedProductList: List<ProductInList>? = emptyList()
    ) {
        val groupedProduct: Map<ProductCategory, List<ProductInList>>? =
            if (foundedProductList?.isEmpty() == true) allProducts?.groupBy { it.product.category }
            else foundedProductList?.groupBy { it.product.category }
    }

    //INIT
    init {
        _state.update { currentState ->
            currentState.copy(
                productCategories = ProductCategory.entries,
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

    fun updateProductInList(productId: UUID) {
        val foundedProduct = _state.value.allProducts?.find { it.id == productId }
        if (foundedProduct == null) return
        val updatedProduct = foundedProduct.copy(isPurchased = !foundedProduct.isPurchased)
        viewModelScope.launch {
            updateProductInListUseCase.invoke(updatedProduct)
            getProductListsWithDetailsUseCase.invoke().collect { productList ->
                _state.value = _state.value.copy(allProducts = productList.first().products)
            }
        }
    }

    fun deleteProductInList(productId: UUID) {
        val foundedProduct = _state.value.allProducts?.find { it.id == productId }
        if (foundedProduct == null) return
        viewModelScope.launch {
            deleteProductInListUseCase.invoke(foundedProduct)
        }
        onMessageProductDeleted()
    }

    fun deletePurchasedProducts() {
        val purchasedProducts = _state.value.allProducts?.filter { it.isPurchased }
        viewModelScope.launch(Dispatchers.Default) {
            deleteProductsInListUseCase.invoke(purchasedProducts ?: emptyList())
        }
        onMessageProductDeleted()
    }


    private fun observeProducts(listId: UUID?) {
        if (listId == null) return
        observeJob?.cancel()
        observeJob = viewModelScope.launch {
            getProductInList(listId)
                .scan(emptyList<ProductInList>()) { previousProduct, newProduct ->

                    val hasNewItem =
                        previousProduct.isNotEmpty() && newProduct.size > previousProduct.size

                    _state.update { currentState ->
                        currentState.copy(
                            allProducts = newProduct,
                            userMessage = if (hasNewItem) "Products added successfully" else null
                        )
                    }
                    newProduct
                }.collect()
        }
    }

    fun addProductInList(
        productListId: UUID,
    ) {
        viewModelScope.launch {
            val product = getOrCreateProductUseCase.invoke(
                Product(
                    name = _state.value.dialogInput ?: "ALL",
                    category = _state.value.selectedCategoryFromDialog ?: ProductCategory.ALL
                )
            )

            val result = addProductInListUseCase.invoke(
                listId = _state.value.listId ?: return@launch,
                productInList = ProductInList(
                    productListId = productListId,
                    product = product,
                    isPurchased = false,
                    quantity = _state.value.quantityInputDialog?.toInt() ?: 1
                )
            )
            when (result) {
                is UseCaseResult.Error -> {
                    _state.update { currentState ->
                        currentState.copy(errorMessageDialog = result.error)
                    }
                }

                is UseCaseResult.Success -> {
                    onDismissDialog()
                    onMessageProductAdded()
                }

                is UseCaseResult.Loading -> {}
            }
        }
    }

    fun findProductByProductName(productName: String) {
        searchJob?.cancel()
        searchJob = viewModelScope.launch(Dispatchers.IO) {
            delay(300)

            val listId = _state.value.listId ?: return@launch
            _state.value.listId?.let {
                val products = findProductsInListUseCase.invoke(
                    listId = listId,
                    letter = productName
                )

                _state.update { currentState ->
                    currentState.copy(
                        foundedProductList = products
                    )
                }
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
            currentState.copy(
                activeDialog = UiDialog.NONE,
                selectedCategoryFromDialog = ProductCategory.ALL,
                dialogInput = ""
            )
        }
    }

    fun onInputChange(input: String) {
        _state.update { currentState ->
            currentState.copy(dialogInput = input)
        }
    }

    fun onQuantityChange(input: String) {
        _state.update { currentState ->
            currentState.copy(quantityInputDialog = input)
        }
    }

    fun showConfirmationDialog() {
        _state.update { currentState ->
            currentState.copy(activeDialog = UiDialog.MESSAGE_DIALOG)
        }
    }

    fun changeDialogStateTypeOnMessage() {
        _state.update { currentState ->
            currentState.copy(
                isInputDialogState = false
            )
        }
    }

    fun changeDialogStateTypeOnInput() {
        _state.update { currentState ->
            currentState.copy(
                isInputDialogState = true
            )
        }
    }

    //snackbar
    fun onMessageShown() {
        _state.update { it.copy(userMessage = null) }
    }

    fun onMessageProductAdded() {
        _state.update { currentState ->
            currentState.copy(userMessage = "Product has been added successful")
        }
    }

    fun onMessageProductDeleted() {
        _state.update { currentState ->
            currentState.copy(userMessage = "Product has been deleted successful")
        }
    }

}