package com.example.shoply.domain.usecase.product

import com.example.shoply.data.repository.ProductRepository
import com.example.shoply.domain.model.Product

class DeleteProductsUseCase(
    private val productRepository: ProductRepository
) {
    suspend operator fun invoke(
        products: List<Product>
    ) {
        productRepository.deleteSelectedProducts(products)
    }
}