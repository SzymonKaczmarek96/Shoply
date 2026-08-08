package com.example.shoply.domain.error.productserror


enum class ProductNameValidationError {
    BLANK,
    TOO_LONG,
    ALREADY_EXISTS;

    val message: String
        get() = when (this) {
            BLANK -> "Product name cannot be blank"
            TOO_LONG -> "Product name is too long"
            ALREADY_EXISTS -> "Product with this name already exists"
        }
}

class ProductNameValidationException(val error: ProductNameValidationError) :
    Exception(error.message)
