package com.example.shoply.domain.error.usecaserror

sealed class UseCaseErrorHandler {

    data class UseCaseDuplicationHandler(val message: String) : UseCaseErrorHandler()
    data class UseCaseUnexpectedThrowable(val message: String) : UseCaseErrorHandler()
}