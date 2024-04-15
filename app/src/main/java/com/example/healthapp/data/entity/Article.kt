package com.example.healthapp.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity(tableName = "articles")
data class Article(
    @PrimaryKey(autoGenerate = true)
    val articleID: Int = 0,
    @ColumnInfo(name = "title")
    val title: String,
    @ColumnInfo(name = "category")
    val category: String, // Use TypeConverter for ENUM-like behavior
    @ColumnInfo(name = "content")
    val content: String,
    @ColumnInfo(name = "wordCount")
    val wordCount: Int,
    @ColumnInfo(name = "authorID")
    val authorID: Int,
    @ColumnInfo(name = "publishDate")
    val publishDate: LocalDateTime?,
    @ColumnInfo(name = "lastUpdated")
    val lastUpdated: LocalDateTime?
)
