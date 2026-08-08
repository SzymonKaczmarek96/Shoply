package com.example.shoply.domain.usecase.productinlist

import com.example.shoply.data.repository.ProductInListRepository
import com.example.shoply.domain.model.ProductInList

class DeleteProductsInListUseCase(
    private val productInListRepository: ProductInListRepository
) {
    suspend operator fun invoke(productsInList: List<ProductInList>) {
        productInListRepository.deleteProductsInList(productsInList)
    }

}