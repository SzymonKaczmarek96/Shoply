package com.example.shoply.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.shoply.domain.model.Role
import com.example.shoply.domain.model.User
import java.util.UUID

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey
    val userId: UUID = UUID.randomUUID(),
    val name: String,
    val email: String,
    val role: Role,
    val profilePictureUrl: String,
)

fun UserEntity.toDomain(): User {
    return User(
        userId = this.userId,
        name = this.name,
        email = this.email,
        role = this.role,
        profilePictureUrl = this.profilePictureUrl
    )
}

