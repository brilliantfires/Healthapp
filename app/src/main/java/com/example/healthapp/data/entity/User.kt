package com.example.healthapp.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

//数据类在 Kotlin 中主要用于保存数据。该类以关键字 data 进行标记。
@Entity(tableName = "users")
data class User(
    @PrimaryKey(autoGenerate = true)
    val userId: Int = 0,
    val username: String,
    val passwordHash: String,
    val email: String,
    val createDate: String
)