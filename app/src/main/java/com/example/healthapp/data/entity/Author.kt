package com.example.healthapp.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "authors")
data class Author(
    @PrimaryKey(autoGenerate = true)
    val authorID: Int = 0,
    @ColumnInfo(name = "name")
    val name: String,
    @ColumnInfo(name = "bio")
    val bio: String,
    @ColumnInfo(name = "profilePicture")
    val profilePicture: String // 存储图片的地址
)
