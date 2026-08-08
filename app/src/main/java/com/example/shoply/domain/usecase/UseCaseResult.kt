package com.example.shoply.domain.usecase

sealed class UseCaseResult<out T, out E> {
    object Loading : UseCaseResult<Nothing, Nothing>()
    data class Success<T>(val data: T) : UseCaseResult<T, Nothing>()
    data class Error<E>(val error: E) : UseCaseResult<Nothing, E>()

}