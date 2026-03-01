package com.example.shoply.domain.usecase

import com.example.shoply.domain.model.Product
import com.example.shoply.domain.model.ProductList
import com.example.shoply.domain.model.Role
import com.example.shoply.domain.model.User

class GetProductListUseCase {

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

    val product1 = Product(
        name = "Milk",
        isPurchased = false
    )

    val product2 = Product(
        name = "Bread",
        isPurchased = true
    )

//    val uuid: UUID = UUID.randomUUID(),
//    val name: String,
//    val description: String,
//    val icon: Int,


    val productList = listOf(
        ProductList(
            name = "Groceries",
            items = listOf(
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