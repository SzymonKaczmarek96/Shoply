package com.example.shoply.presentation.screens.homescreen

import androidx.lifecycle.ViewModel
import com.example.shoply.domain.model.ProductList
import com.example.shoply.domain.model.Role
import com.example.shoply.domain.model.User
import com.example.shoply.domain.usecase.GetProductListUseCase
import com.example.shoply.presentation.components.dialogs.UiDialog
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.util.UUID

class HomeScreenViewModel(
    private val getProductListUseCase: GetProductListUseCase = GetProductListUseCase(),
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
        _state.update {
            it.copy(
                shopList = it.shopList?.minus(foundedShopList),
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
        _state.update {
            it.copy(
                shopList = it.shopList?.plus(
                    ProductList(
                        name = name, products = emptyList(), members = listOf(
                            User(
                                name = "Alice Johnson",
                                email = "123@gmail.com",
                                role = Role.CREATOR,
                            )
                        )
                    )
                ),
                activeDialog = UiDialog.NONE,
                dialogInput = "",
                userMessage = "$name list created"
            )
        }
    }

    //init

    init {
        _state.update { currentState ->
            currentState.copy(
                shopList = getProductListUseCase.productList,
            )
        }
    }
}