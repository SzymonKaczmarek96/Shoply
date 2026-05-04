package com.example.shoply.domain.usecase.productinlist

import com.example.shoply.data.repository.ProductInListRepository
import com.example.shoply.domain.model.ProductInList

class AddProductInListUseCase(
    private val productInListRepository: ProductInListRepository
) {
    suspend operator fun invoke(productInList: ProductInList) {
        productInListRepository.addProductInList(productInList)
    }
}