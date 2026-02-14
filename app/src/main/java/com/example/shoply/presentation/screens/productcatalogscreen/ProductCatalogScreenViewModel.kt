package com.example.shoply.presentation.screens.productcatalogscreen

import androidx.lifecycle.ViewModel
import com.example.shoply.domain.model.Product
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.UUID

class ProductCatalogScreenViewModel(
    private val testUseCase: TestUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(State())
    val state: StateFlow<State> = _state.asStateFlow()

    data class State(
        val products: List<Product> = emptyList()
    )

    fun onProductCheckedChange(productId: UUID, isChecked: Boolean) {
        val updatedProducts = _state.value.products.map {
            if (it.uuid == productId) {
                it.copy(isSelected = isChecked)
            } else {
                it
            }
        }
        _state.value = _state.value.copy(products = updatedProducts)
    }

    init {
        _state.value = state.value.copy(
            products = testUseCase.products
        )
    }


}