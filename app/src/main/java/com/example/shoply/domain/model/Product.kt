package com.example.shoply.domain.model

import java.util.UUID

data class Product(
    val uuid: UUID = UUID.randomUUID(),
    val name: String,
    val description: String,
    val icon: Int,
    val isPurchased: Boolean = false,
) {
}