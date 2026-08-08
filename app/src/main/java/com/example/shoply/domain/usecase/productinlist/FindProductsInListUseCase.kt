package com.example.shoply.domain.usecase.productinlist

import com.example.shoply.data.repository.ProductInListRepository
import com.example.shoply.domain.model.ProductInList
import java.util.UUID

class FindProductsInListUseCase(
    private val productsInListRepository: ProductInListRepository
) {

    suspend operator fun invoke(listId: UUID, letter: String): List<ProductInList> {
        return productsInListRepository.findContainingProducts(listId, letter)
    }
}