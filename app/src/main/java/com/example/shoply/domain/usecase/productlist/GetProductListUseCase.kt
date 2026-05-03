package com.example.shoply.domain.usecase.productlist

import com.example.shoply.data.repository.ProductListRepository
import com.example.shoply.domain.model.ProductList
import kotlinx.coroutines.flow.Flow

class GetProductListUseCase(
    private val productListRepository: ProductListRepository
) {
    operator fun invoke(): Flow<List<ProductList>> {
        return productListRepository.getAllProductList()
    }
}