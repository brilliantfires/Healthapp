package com.example.healthapp.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "articleTags")
data class ArticleTag(
    @PrimaryKey(autoGenerate = true)
    val tagID: Int = 0,
    @ColumnInfo(name = "tagName")
    val tagName: String
)
