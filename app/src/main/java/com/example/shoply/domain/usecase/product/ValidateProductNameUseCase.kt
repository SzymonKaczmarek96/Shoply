package com.example.shoply.domain.usecase.product

import com.example.shoply.data.repository.ProductRepository
import com.example.shoply.domain.error.productserror.ProductNameValidationError
import com.example.shoply.domain.error.productserror.ProductNameValidationException
import com.example.shoply.domain.usecase.UseCaseResult

class ValidateProductNameUseCase(
    private val productRepository: ProductRepository
) {
    suspend operator fun invoke(name: String): UseCaseResult<Unit, ProductNameValidationException> {

        if (name.isBlank()) {
            throw ProductNameValidationException(ProductNameValidationError.BLANK)
        }

        if (name.length > 30) {
            throw ProductNameValidationException(ProductNameValidationError.TOO_LONG)

        }

        val exists = productRepository.existsByName(name)
        if (exists) {
            throw ProductNameValidationException(ProductNameValidationError.ALREADY_EXISTS)
        }

        return UseCaseResult.Success(Unit)
    }
}



