package com.example.shoply.domain.usecase.product

import com.example.shoply.domain.model.ProductCategory
import com.example.shoply.domain.model.ProductInList
import com.example.shoply.domain.model.ProductList
import com.example.shoply.domain.model.Role
import com.example.shoply.domain.model.User
import java.util.UUID

class Test {

    // val id: UUID = UUID.randomUUID(),
    //    val name: String,
    //    val email: String,
    //    val role: Role,
    //    val profilePictureUrl: String?,

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
        name = "Milk",
        productListId = UUID.randomUUID(),
        category = ProductCategory.OTHER,
    )

    val product2 = ProductInList(
        name = "Bread",
        productListId = UUID.randomUUID(),
        category = ProductCategory.OTHER,
    )

//    val uuid: UUID = UUID.randomUUID(),
//    val name: String,
//    val description: String,
//    val icon: Int,


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