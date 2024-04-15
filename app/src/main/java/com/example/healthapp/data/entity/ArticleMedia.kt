package com.example.healthapp.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName = "articleMedia",
    foreignKeys = [
        ForeignKey(
            entity = Article::class,
            parentColumns = arrayOf("articleID"),
            childColumns = arrayOf("articleID"),
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class ArticleMedia(
    @PrimaryKey(autoGenerate = true)
    val mediaID: Int = 0,
    @ColumnInfo(name = "articleID")
    val articleID: Int,
    @ColumnInfo(name = "mediaType")
    val mediaType: String, // Use TypeConverter for ENUM-like behavior
    @ColumnInfo(name = "filePath")
    val filePath: String,
    @ColumnInfo(name = "description")
    val description: String
)
