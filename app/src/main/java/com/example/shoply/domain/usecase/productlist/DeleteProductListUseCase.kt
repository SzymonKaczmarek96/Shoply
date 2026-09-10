package com.example.shoply.domain.usecase.productlist

import android.util.Log
import com.example.shoply.data.repository.ProductListRepository
import com.example.shoply.domain.error.usecaserror.UseCaseErrorHandler
import com.example.shoply.domain.model.ProductList
import com.example.shoply.domain.usecase.UseCaseResult
import kotlinx.coroutines.CancellationException

class DeleteProductListUseCase(
    private val productListRepository: ProductListRepository
) {

    suspend operator fun invoke(
        productList: ProductList,
    ): UseCaseResult<Unit, UseCaseErrorHandler> {
        return try {
            UseCaseResult.Success(productListRepository.deleteProductList(productList))
        } catch (e: Throwable) {
            UseCaseResult.Error(determineUseCaseError(e))
        }
    }

    private fun determineUseCaseError(exception: Throwable): UseCaseErrorHandler {
        return when (exception) {
            is CancellationException -> {
                Log.d("DeleteProductListUseCase", "Operation cancelled")
                throw CancellationException(message = exception.message, cause = exception)
            }

            is IllegalArgumentException -> {
                UseCaseErrorHandler.UseCaseDuplicationHandler("Illegal Argument Exception")
            }

            else -> {
                UseCaseErrorHandler.UseCaseUnexpectedThrowable("An unexpected error occurred")
            }
        }
    }
//    suspend operator fun invoke(productList: ProductList): UseCaseResult<Unit, Throwable> {
    //        return try {
    //            UseCaseResult.Success(productListRepository.deleteProductList(productList))
//        } catch(e: IOException){
//            UseCaseResult.Error(e)
//        } catch (e: SQLException){
//            UseCaseResult.Error(e)
//        }
//    }
}