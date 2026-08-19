package com.example.shoply.presentation.screens.productcatalogscreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shoply.domain.model.Product
import com.example.shoply.domain.model.ProductCategory
import com.example.shoply.domain.model.ProductInList
import com.example.shoply.domain.usecase.UseCaseResult
import com.example.shoply.domain.usecase.product.ChangeProductsCategoryUseCase
import com.example.shoply.domain.usecase.product.DeleteProductsUseCase
import com.example.shoply.domain.usecase.product.GetProductUseCase
import com.example.shoply.domain.usecase.product.InsertProductUseCase
import com.example.shoply.domain.usecase.product.UpdateProductUseCase
import com.example.shoply.domain.usecase.productinlist.AddProductsInListUseCase
import com.example.shoply.domain.usecase.productinlist.GetProductInListUseCase
import com.example.shoply.presentation.components.dialogs.UiDialog
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID

class ProductCatalogScreenViewModel(
    private val getProductUseCase: GetProductUseCase,
    private val insertProductUseCase: InsertProductUseCase,
    private val addProductsInListUseCase: AddProductsInListUseCase,
    private val getProductInList: GetProductInListUseCase,
    private val deleteProductUseCase: DeleteProductsUseCase,
    private val updateProductUseCase: UpdateProductUseCase,
    private val changeProductsCategoryUseCase: ChangeProductsCategoryUseCase,
    private val idDispatcher: CoroutineDispatcher = Dispatchers.IO,
    private val stateInit: State = State()
) : ViewModel() {
    private val _state = MutableStateFlow(stateInit)
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
        val isLastScreenProductListScreen: Boolean? = false,
        val transferProductList: List<ProductInList>? = emptyList(),
        val isUpdateDialog: Boolean = false,
        val isCategoryUpdate: Boolean = false,
        val isError: Boolean = false,
        val isSuccess: Boolean = false,
        val isLoading: Boolean = false,
        val selectedProductId: UUID? = null,
    ) {
        val items: List<Product> =
            if (selectedCategoryFromFilterCategory == ProductCategory.ALL) {
                allProducts
            } else {
                allProducts.filter { it.category == selectedCategoryFromFilterCategory }
            }
    }

    // businnes logic
    fun addSelectedProductList(listId: UUID) {
        viewModelScope.launch(idDispatcher) {

            val getAllProductsInList = getProductInList.invoke(listId = listId).first()

            _state.update { currentState ->
                currentState.copy(
                    transferProductList = getAllProductsInList
                )
            }

            val selectedProducts = _state.value.selectedIds
            val transferredProducts =
                _state.value.transferProductList?.map { it.product } ?: emptyList()
            val notExistingProduct =
                selectedProducts.filter { uuid -> transferredProducts.none { it.productId == uuid } }

            val productsForAdd =
                _state.value.allProducts.filter { product -> notExistingProduct.any { it == product.productId } }

            val createProductInList = productsForAdd.map {
                ProductInList(
                    productListId = listId,
                    product = it,
                )
            }
            addProductsInListUseCase.invoke(createProductInList)
        }
    }

    fun setIsLastScreen(showSpecialIcon: Boolean) {
        _state.update { currentState ->
            currentState.copy(
                isLastScreenProductListScreen = showSpecialIcon
            )
        }
    }

    fun deleteProducts() {

        viewModelScope.launch(idDispatcher) {
            val selectedProducts = _state.value.selectedIds
            val foundedProducts =
                _state.value.allProducts.filter { product -> selectedProducts.contains(product.productId) }

            _state.update { it.loading() }

            when (val result = deleteProductUseCase(foundedProducts)) {
                is UseCaseResult.Success ->
                    _state.update { it.success(result.data) }

                is UseCaseResult.Error ->
                    _state.update { it.error(result.error) }

                else -> {
                    _state.update { it.error("Something went wrong") }
                }
            }
        }
    }

    //dialog operation
    fun confirmInput() {
        val isUpdateDialog = _state.value.isUpdateDialog
        if (isUpdateDialog) {
            updateProduct()
            return
        }
        addProduct()

    }

    fun selectProduct(productId: UUID) {
        _state.update { currentState ->
            currentState.copy(
                selectedProductId = productId
            )
        }
    }

    fun setDialogTypeOnUpdate(isUpdateDialog: Boolean) {
        _state.update { currentState ->
            currentState.copy(
                isUpdateDialog = isUpdateDialog
            )
        }
    }

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
        _state.update {
            it.copy(
                activeDialog = UiDialog.NONE,
                selectedCategoryFromDialog = ProductCategory.ALL,
                dialogInput = "",
                dialogError = null,
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

    fun changeCategoryToFavorite() {
        val selectedProducts = _state.value.allProducts.filter {
            _state.value.selectedIds.contains(it.productId)
        }

        val productsForUpdate =
            selectedProducts.map { it.copy(category = ProductCategory.FAVORITE) }
        viewModelScope.launch(idDispatcher) {
            val result = changeProductsCategoryUseCase.invoke(productsForUpdate)
            changeProductsCategoryUseCaseHandler(result)
        }
    }

    fun showUpdateCategoryDialog() {
        if (hasAnySelectedItems()) {
            setCategoryUpdate(true)
            onCreateDialog()
        }
    }

    fun changeCategoryForSelectedCategory() {
        val selectedProducts = _state.value.allProducts.filter {
            _state.value.selectedIds.contains(it.productId)
        }
        val productsForUpdate =
            selectedProducts.map { it.copy(category = _state.value.selectedCategoryFromDialog) }
        viewModelScope.launch(idDispatcher) {
            val result = changeProductsCategoryUseCase.invoke(productsForUpdate)
            changeProductsCategoryUseCaseHandler(result)
        }
        dismissDialog()
    }

    fun setCategoryUpdate(isCategoryUpdate: Boolean) {
        _state.update {
            it.copy(isCategoryUpdate = isCategoryUpdate)
        }
    }

    private fun hasAnySelectedItems() = _state.value.selectedIds.isNotEmpty()

    // init
    init {
        viewModelScope.launch {
            getProductUseCase.invoke()
                .collect {
                val existingProductCategory: Set<ProductCategory> =
                    LinkedHashSet(it.map { p -> p.category } + ProductCategory.ALL)
                _state.update { currentState ->
                    currentState.copy(
                        allProducts = it,
                        existingProductCategory = existingProductCategory,
                        productCategories = ProductCategory.entries,
                    )
                }
            }
        }
    }

    // private
    private fun changeProductsCategoryUseCaseHandler(
        result: UseCaseResult<Unit, Throwable>
    ) {
        when (result) {
            is UseCaseResult.Error -> {
                _state.update {
                    it.error(result.error.message ?: "Something went wrong")
                }
            }

            is UseCaseResult.Success -> {
                _state.update {
                    it.success(
                        message = "Category updated successfully"
                    )
                }
            }

            is UseCaseResult.Loading -> {
                _state.update { it.loading() }
            }
        }
    }

    private fun updateProduct() {
        viewModelScope.launch(idDispatcher) {
            val product =
                _state.value.allProducts.find { it.productId == _state.value.selectedProductId }
                    ?: return@launch
            val updatedProduct = product.copy(
                name = _state.value.dialogInput,
                category = _state.value.selectedCategoryFromDialog
            )
            val result = updateProductUseCase.invoke(updatedProduct)
            when (result) {
                is UseCaseResult.Success -> {
                    onProductSaved(
                        onSuccess = { dismissDialog() },
                    )
                }

                is UseCaseResult.Error -> {
                    errorResult(result)
                }

                is UseCaseResult.Loading -> {
                    _state.update { it.loading() }
                }
            }
        }
    }

    private fun addProduct() {
        viewModelScope.launch {
            val product = Product(
                name = _state.value.dialogInput,
                category = _state.value.selectedCategoryFromDialog
            )

            val result = insertProductUseCase.invoke(product)
            when (result) {
                is UseCaseResult.Success -> {
                    onProductSaved(
                        onSuccess = { dismissDialog() },
                    )
                }

                is UseCaseResult.Error -> {
                    errorResult(result)
                }

                UseCaseResult.Loading -> {
                    _state.update { it.loading() }
                }
            }
        }
    }

    private fun onProductSaved(
        onSuccess: () -> Unit,
    ) {
        val mappedCategory = mapToCategory(_state.value.selectedCategoryFromDialog.name)
        _state.update { currentState ->
            currentState
                .success("Successfully saved product")
                .copy(
                    existingProductCategory =
                        if (!hasCategory(mappedCategory.name)) currentState.existingProductCategory + mappedCategory
                        else currentState.existingProductCategory,
                )
        }
        onSuccess()
    }

    private fun errorResult(result: UseCaseResult.Error<Throwable>) {
        _state.update {
            it.copy(
                isLoading = false,
                isSuccess = false,
                isError = true,
                dialogError = result.error.message
            )
        }
    }

    private fun mapToCategory(value: String): ProductCategory {
        return ProductCategory.entries.first { it.name == value }
    }

    private fun hasCategory(categoryName: String) = _state.value
        .existingProductCategory.any { it.name == categoryName }

    // UseCaseResultHandling

    fun State.loading() = copy(
        isLoading = true,
        isSuccess = false,
        isError = false
    )

    fun State.success(message: String? = null) = copy(
        isLoading = false,
        isSuccess = true,
        isError = false,
        userMessage = message
    )

    fun State.error(message: String) = copy(
        isLoading = false,
        isSuccess = false,
        isError = true,
        userMessage = message
    )


}

