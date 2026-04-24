package com.example.shoply.domain.model

import com.example.shoply.data.model.UserEntity
import java.util.UUID

data class User(
    val userId: UUID = UUID.randomUUID(),
    val name: String,
    val email: String,
    val role: Role,
    val profilePictureUrl: String? = "https://www.gravatar.com/avatar/00000000000000000000000000000000?d=mp&f=y",
)

fun User.toEntity(): UserEntity {
    return UserEntity(
        userId = this.userId,
        name = this.name,
        email = this.email,
        role = this.role,
        profilePictureUrl = this.profilePictureUrl ?: ""
    )
}

enum class Role() {
    CREATOR,
    MEMBER
}
