package com.example.shoply.domain.usecase.productinlist

import com.example.shoply.data.repository.ProductInListRepository
import com.example.shoply.domain.model.ProductInList
import kotlinx.coroutines.flow.Flow
import java.util.UUID

class GetProductInListUseCase(
    private val productInListRepository: ProductInListRepository
) {
    operator fun invoke(listId: UUID): Flow<List<ProductInList>> {
        return productInListRepository.getAllProductInList(listId)
    }
}