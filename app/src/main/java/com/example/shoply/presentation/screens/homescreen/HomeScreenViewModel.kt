package com.example.shoply.presentation.screens.homescreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shoply.domain.error.usecaserror.UseCaseErrorHandler
import com.example.shoply.domain.model.ProductList
import com.example.shoply.domain.model.Role
import com.example.shoply.domain.model.User
import com.example.shoply.domain.usecase.UseCaseResult
import com.example.shoply.domain.usecase.productlist.AddProductListUseCase
import com.example.shoply.domain.usecase.productlist.DeleteProductListUseCase
import com.example.shoply.domain.usecase.productlist.GetProductListsWithDetailsUseCase
import com.example.shoply.presentation.components.dialogs.UiDialog
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID

class HomeScreenViewModel(
    private val addProductListUseCase: AddProductListUseCase,
    private val deleteProductListUseCase: DeleteProductListUseCase,
    private val getProductListsWithDetailsUseCase: GetProductListsWithDetailsUseCase,
    private val dispatcherId: CoroutineDispatcher = Dispatchers.IO,
    private val stateInit: State = State()
) : ViewModel() {

    data class State(
        val shopList: List<ProductList>? = null,
        val activeDialog: UiDialog = UiDialog.NONE,
        val dialogInput: String = "",
        val dialogError: String? = null,
        val userMessage: String? = null,
        val isLoading: Boolean = false,
        val isSuccess: Boolean = false,
        val isError: Boolean = false
    )

    private val _state = MutableStateFlow(stateInit)
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch(dispatcherId) {
            getProductListsWithDetailsUseCase.invoke().collect { productLists ->
                _state.update { currentState ->
                    currentState.copy(
                        shopList = productLists
                    )
                }
            }
        }
    }

    fun onCreateListClick() {
        _state.update { it.copy(activeDialog = UiDialog.INPUT_DIALOG) }
    }

    fun onDialogInputChange(input: String) {
        _state.update { it.copy(dialogInput = input, dialogError = null) }
    }

    fun onDialogConfirm() {
        when (_state.value.activeDialog) {
            UiDialog.INPUT_DIALOG -> createList()
            UiDialog.NONE -> Unit
            UiDialog.MESSAGE_DIALOG -> Unit
        }
    }

    fun onDialogDismiss() {
        _state.update {
            it.copy(
                activeDialog = UiDialog.NONE,
                dialogInput = "",
                dialogError = null
            )
        }
    }

    fun onMessageShown() {
        _state.update { it.copy(userMessage = null) }
    }

    fun deleteShopList(productListId: UUID) {
        val foundedShopList =
            _state.value.shopList?.find { it.productListId == productListId } ?: return
        viewModelScope.launch(dispatcherId) {
            _state.update { it.loading() }
            try {
                delay(
                    10000
                )
                val result = deleteProductListUseCase.invoke(foundedShopList)
                when (result) {
                    is UseCaseResult.Success -> _state.update { it.success("${foundedShopList.name} list deleted") }
                    is UseCaseResult.Error -> _state.update { it.error(determineErrorMessage(result.error)) }
                    else -> _state.update { it.copy(isLoading = false) }
                }
            } finally {
                _state.update { it.copy(isLoading = false) }
            }
        }
    }

    private fun determineErrorMessage(errorHandler: UseCaseErrorHandler): String {
        return when (errorHandler) {
            is UseCaseErrorHandler.UseCaseDuplicationHandler -> errorHandler.message
            is UseCaseErrorHandler.UseCaseUnexpectedThrowable -> errorHandler.message
        }
    }

    private fun createList() {
        val name = _state.value.dialogInput
        if (_state.value.shopList?.any { it.name.equals(name, ignoreCase = true) } ?: false) {
            _state.update { it.copy(dialogError = "A list with this name already exists") }
            return
        }
        viewModelScope.launch(dispatcherId) {
            val productList = ProductList(
                name = name,
                products = emptyList(),
                members = listOf(
                    User(
                        name = "Alice Johnson",
                        email = "123@gmail.com",
                        role = Role.CREATOR,
                    )
                )
            )
            val result = addProductListUseCase.invoke(productList = productList)
            when (result) {
                is UseCaseResult.Success -> _state.update { it.success("$name has been added successfully") }
                is UseCaseResult.Error -> _state.update { it.error(result.error) }
                is UseCaseResult.Loading -> _state.update { it.loading() }
            }
        }
    }

    private fun State.loading() = copy(
        isLoading = true,
        isSuccess = false,
        isError = false
    )

    private fun State.success(message: String? = null) = copy(
        isLoading = false,
        isSuccess = true,
        isError = false,
        userMessage = message
    )

    private fun State.error(message: String) = copy(
        isLoading = false,
        isSuccess = false,
        isError = true,
        userMessage = message
    )
}