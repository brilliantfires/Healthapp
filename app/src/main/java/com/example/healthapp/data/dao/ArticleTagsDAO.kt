package com.example.healthapp.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.healthapp.data.entity.ArticleTag
import kotlinx.coroutines.flow.Flow

@Dao
interface ArticleTagsDAO {
    @Insert
    fun insertArticleTag(articleTag: ArticleTag)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(articleTags: List<ArticleTag>)

    @Update
    fun updateArticleTag(articleTag: ArticleTag)

    @Delete
    fun deleteArticleTag(articleTag: ArticleTag)

    @Query("SELECT * FROM articleTags")
    fun getAllTags(): Flow<List<ArticleTag>>

    @Query("SELECT * FROM articleTags")
    suspend fun getAllTagsN(): List<ArticleTag>

    // 通过tagID来找Tag
    @Query("SELECT * FROM articleTags WHERE tagID =:tagId")
    fun getTagById(tagId: Int): Flow<ArticleTag>
}
