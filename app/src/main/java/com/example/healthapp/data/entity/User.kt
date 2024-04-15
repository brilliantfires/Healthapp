package com.example.healthapp.data.entity


import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime

//数据类在 Kotlin 中主要用于保存数据。该类以关键字 data 进行标记。
// 这里是User的表，里面存放了用户的主要信息
@Entity(tableName = "users")
data class User(
    @PrimaryKey(autoGenerate = true)
    val userId: Int = 0,
    @ColumnInfo(name = "username")
    val username: String,
    @ColumnInfo(name = "email")
    val email: String,
    @ColumnInfo(name = "phoneNumber")
    val phoneNumber: String?,
    @ColumnInfo(name = "passwordHash")
    val passwordHash: String,
    @ColumnInfo(name = "profilePicture")
    val profilePicture: String,     // 存储图片的地址
    @ColumnInfo(name = "role")
    val role: String, // Could be better handled with a TypeConverter for ENUM
    @ColumnInfo(name = "bloodType")
    val bloodType: String?, // Same here for ENUM
    @ColumnInfo(name = "gender")
    val gender: String?, // And here
    @ColumnInfo(name = "dateOfBirth")
    val dateOfBirth: LocalDateTime?,
    @ColumnInfo(name = "dateCreated", defaultValue = "CURRENT_TIMESTAMP")
    val dateCreated: LocalDateTime = LocalDateTime.now(),
    @ColumnInfo(name = "lastLogin")
    val lastLogin: LocalDateTime?,
    @ColumnInfo(name = "isWheelchairUser")
    val isWheelchairUser: Boolean?,
    @ColumnInfo(name = "skinType")
    val skinType: String?, // Consider TypeConverter for ENUM
    @ColumnInfo(name = "heartRateAffectingDrugs")
    val heartRateAffectingDrugs: String?
)
