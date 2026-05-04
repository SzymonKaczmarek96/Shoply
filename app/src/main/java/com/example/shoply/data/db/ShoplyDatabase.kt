package com.example.shoply.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.shoply.data.converters.Converters
import com.example.shoply.data.dao.ProductDao
import com.example.shoply.data.dao.ProductInListDao
import com.example.shoply.data.dao.ProductListDao
import com.example.shoply.data.dao.UserDao
import com.example.shoply.data.model.ProductEntity
import com.example.shoply.data.model.ProductInListEntity
import com.example.shoply.data.model.ProductListEntity
import com.example.shoply.data.model.ProductListMemberCrossReference
import com.example.shoply.data.model.UserEntity

@Database(
    entities = [ProductEntity::class, ProductListEntity::class,
        UserEntity::class, ProductListMemberCrossReference::class, ProductInListEntity::class],
    version = 3, exportSchema = false
)
@TypeConverters(Converters::class)
abstract class ShoplyDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun productDao(): ProductDao

    abstract fun productListDao(): ProductListDao

    abstract fun productInListDao(): ProductInListDao
}