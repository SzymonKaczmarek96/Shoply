package com.example.shoply.domain.model

import java.util.UUID

data class User(
    val id: UUID = UUID.randomUUID(),
    val name: String,
    val email: String,
    val role: Role,
    val profilePictureUrl: String? = "https://www.gravatar.com/avatar/00000000000000000000000000000000?d=mp&f=y",
) {}


enum class Role() {
    CREATOR,
    MEMBER
}
