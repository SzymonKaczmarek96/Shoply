package com.example.shoply.domain.usecase.productinlist

import com.example.shoply.data.repository.ProductInListRepository
import com.example.shoply.domain.model.ProductInList
import com.example.shoply.domain.usecase.UseCaseResult
import java.util.UUID

class AddProductInListUseCase(
    private val productInListRepository: ProductInListRepository,

) {
    suspend operator fun invoke(
        listId: UUID,
        productInList: ProductInList
    ): UseCaseResult<Unit, String> {
        if (validateProductInList(listId, productInList)) {
            return UseCaseResult.Error("Product already exists in the list")
        }
        val result = productInListRepository.addProductInList(productInList)
        return UseCaseResult.Success(result)
    }

    private suspend fun validateProductInList(listId: UUID, productInList: ProductInList): Boolean {
        return productInListRepository.isExistsProductInList(listId, productInList.product.name)
    }
}