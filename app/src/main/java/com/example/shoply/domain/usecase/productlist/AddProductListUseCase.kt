package com.example.shoply.domain.usecase.productlist

import com.example.shoply.data.repository.ProductListRepository
import com.example.shoply.domain.model.ProductList
import kotlinx.coroutines.flow.first

class AddProductListUseCase(
    private val productListRepository: ProductListRepository
) {
    suspend operator fun invoke(productList: ProductList) {
        val existingLists = productListRepository.getAllProductList().first()

        val isDuplicate = existingLists.any { list ->
            list.name.equals(productList.name, ignoreCase = true)
        }

        if (isDuplicate) {
            throw IllegalArgumentException("Product list with name '${productList.name}' already exists")
        }
        productListRepository.addProductList(productList)
    }
}
