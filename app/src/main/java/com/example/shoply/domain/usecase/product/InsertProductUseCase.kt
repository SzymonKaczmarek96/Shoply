package com.example.shoply.domain.usecase.product

import com.example.shoply.data.repository.ProductRepository
import com.example.shoply.domain.error.productserror.ProductNameValidationException
import com.example.shoply.domain.model.Product
import com.example.shoply.domain.usecase.UseCaseResult
import java.io.IOException
import java.sql.SQLException

class InsertProductUseCase(
    private val productRepository: ProductRepository,
    private val validateProductNameUseCase: ValidateProductNameUseCase
) {
    suspend operator fun invoke(product: Product): UseCaseResult<Unit, Throwable> {

        return try {
            validateProductNameUseCase(product.name)
            productRepository.addProducts(product)
            return UseCaseResult.Success(Unit)
        } catch (e: IOException) {
            UseCaseResult.Error(e)
        } catch (e: SQLException) {
            UseCaseResult.Error(e)
        } catch (e: ProductNameValidationException) {
            UseCaseResult.Error(e)
        }
    }
}