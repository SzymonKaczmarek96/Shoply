package com.example.shoply.data.converters

import androidx.room.TypeConverter
import com.example.shoply.domain.model.ProductCategory
import com.example.shoply.domain.model.Role
import java.util.UUID

class Converters {
    @TypeConverter
    fun fromUUID(uuid: UUID?): String? = uuid?.toString()

    @TypeConverter
    fun toUUID(value: String?): UUID? = value?.let { UUID.fromString(it) }

    @TypeConverter
    fun fromRole(role: Role?): String? = role?.toString()

    @TypeConverter
    fun toRole(value: String?): Role? = value?.let { Role.valueOf(it) }

    @TypeConverter
    fun fromCategory(category: ProductCategory?): String? = category?.toString()

    @TypeConverter
    fun toCategory(value: String?): ProductCategory? = value?.let { ProductCategory.valueOf(it) }
}