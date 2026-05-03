package com.example.shoply.domain.usecase.product

import com.example.shoply.data.repository.ProductRepository
import com.example.shoply.domain.model.Product

class InsertProductUseCase(
    private val productRepository: ProductRepository
) {
    suspend operator fun invoke(vararg products: Product) {
        if (products.isEmpty()) return
        productRepository.addProducts(products = products)
    }
}