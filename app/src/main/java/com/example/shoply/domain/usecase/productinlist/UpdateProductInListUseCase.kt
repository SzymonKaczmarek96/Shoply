package com.example.shoply.domain.usecase.productinlist

import com.example.shoply.data.repository.ProductInListRepository
import com.example.shoply.domain.model.ProductInList
import com.example.shoply.domain.usecase.UseCaseResult

class UpdateProductInListUseCase(
    private val productInListRepository: ProductInListRepository
) {
    suspend operator fun invoke(productInList: ProductInList): UseCaseResult<Unit, Throwable> {
        return try {
            productInListRepository.updateProductInList(productInList)
            UseCaseResult.Success(Unit)
        } catch (e: Throwable) {
            UseCaseResult.Error(e)
        }
    }
}