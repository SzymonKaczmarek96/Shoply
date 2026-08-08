package com.example.shoply.presentation.screens.homescreen

import com.example.shoply.domain.model.Product
import com.example.shoply.domain.model.ProductInList
import com.example.shoply.domain.model.ProductList
import com.example.shoply.domain.model.Role
import com.example.shoply.domain.model.User
import java.util.UUID

class HomeScreenPreviewData {
    val user1 = User(
        name = "Alice Johnson",
        email = "123@gmail.com",
        role = Role.CREATOR,
    )

    val user2 = User(
        name = "Alice Woman",
        email = "123@gmail.com",
        role = Role.MEMBER,
        profilePictureUrl = "https://randomuser.me/api/portraits/women/2.jpg"
    )

    val user3 = User(
        name = "Alice Man",
        email = "123@gmail.com",
        role = Role.MEMBER,
        profilePictureUrl = "https://randomuser.me/api/portraits/women/8.jpg"
    )

    val product1 = ProductInList(
        productListId = UUID.randomUUID(),
        product = Product(
            name = "Product",
        )
    )

    val product2 = ProductInList(
        productListId = UUID.randomUUID(),
        product = Product(
            name = "Product",
        )
    )

    val productList = listOf(
        ProductList(
            name = "Groceries",
            products = listOf(
                product1,
                product2
            ),
            members = listOf(
                user1,
                user2,
                user3
            ),
        )
    )
}