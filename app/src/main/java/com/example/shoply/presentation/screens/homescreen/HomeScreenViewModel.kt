package com.example.shoply.presentation.screens.homescreen

import androidx.lifecycle.ViewModel
import com.example.shoply.domain.model.ProductList
import com.example.shoply.domain.usecase.GetProductListUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class HomeScreenViewModel(
    private val getProductListUseCase: GetProductListUseCase = GetProductListUseCase(),
) : ViewModel() {

    data class State(
        val products: List<ProductList>? = null,
    )

    private val _state = MutableStateFlow(State())
    val state = _state.asStateFlow()

    init {
        _state.update { currentState ->
            currentState.copy(
                products = getProductListUseCase.productList,
            )
        }
    }
}