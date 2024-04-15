package com.example.healthapp.data.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index

@Entity(
    tableName = "articleTagRelation",
    primaryKeys = ["articleID", "tagID"],
    foreignKeys = [
        ForeignKey(
            entity = Article::class,
            parentColumns = ["articleID"],
            childColumns = ["articleID"]
        ),
        ForeignKey(entity = ArticleTag::class, parentColumns = ["tagID"], childColumns = ["tagID"])
    ],
    indices = [Index(value = ["articleID", "tagID"], unique = true)]
)
data class ArticleTagRelation(
    val articleID: Int,
    val tagID: Int,
    val isDisplayed: Boolean
)
