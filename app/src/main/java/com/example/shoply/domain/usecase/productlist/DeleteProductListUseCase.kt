package com.example.shoply.domain.usecase.productlist

import com.example.shoply.data.repository.ProductListRepository
import com.example.shoply.domain.model.ProductList

class DeleteProductListUseCase(
    private val productListRepository: ProductListRepository
) {
    suspend operator fun invoke(productList: ProductList) {
        productListRepository.deleteProductList(productList)
    }
}