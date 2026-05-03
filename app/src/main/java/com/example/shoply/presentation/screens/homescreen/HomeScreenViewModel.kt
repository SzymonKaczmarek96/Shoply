package com.example.shoply.presentation.screens.homescreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shoply.domain.model.ProductList
import com.example.shoply.domain.model.Role
import com.example.shoply.domain.model.User
import com.example.shoply.domain.usecase.productlist.AddProductListUseCase
import com.example.shoply.domain.usecase.productlist.DeleteProductListUseCase
import com.example.shoply.domain.usecase.productlist.GetProductListUseCase
import com.example.shoply.presentation.components.dialogs.UiDialog
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID

class HomeScreenViewModel(
    private val getProductListUseCase: GetProductListUseCase,
    private val addProductListUseCase: AddProductListUseCase,
    private val deleteProductListUseCase: DeleteProductListUseCase
) : ViewModel() {


    data class State(
        val shopList: List<ProductList>? = null,
        val activeDialog: UiDialog = UiDialog.NONE,
        val dialogInput: String = "",
        val dialogError: String? = null,
        val userMessage: String? = null
    )

    private val _state = MutableStateFlow(State())
    val state = _state.asStateFlow()

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
        viewModelScope.launch {
            deleteProductListUseCase.invoke(foundedShopList)
        }
        _state.update {
            it.copy(
                userMessage = "${foundedShopList.name} list deleted"
            )
        }
    }

    private fun createList() {
        val name = _state.value.dialogInput
        if (_state.value.shopList?.any { it.name.equals(name, ignoreCase = true) } ?: false) {
            _state.update { it.copy(dialogError = "A list with this name already exists") }
            return
        }
        viewModelScope.launch {
            addProductListUseCase.invoke(
                ProductList(
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
            )
        }
        _state.update { currentState ->
            currentState.copy(userMessage = "$name has been added successful")
        }
    }

    //init

    init {
        viewModelScope.launch {
            getProductListUseCase.invoke().collect { productLists ->
                _state.update { currentState ->
                    currentState.copy(
                        shopList = productLists
                    )
                }
            }
        }
    }
}