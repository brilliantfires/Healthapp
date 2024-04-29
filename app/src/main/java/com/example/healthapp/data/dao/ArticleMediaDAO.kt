package com.example.healthapp.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.healthapp.data.entity.ArticleMedia
import kotlinx.coroutines.flow.Flow

@Dao
interface ArticleMediaDAO {
    @Insert
    fun insertArticleMedia(articleMedia: ArticleMedia)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(articleMedia: List<ArticleMedia>)

    @Update
    fun updateArticleMedia(articleMedia: ArticleMedia)

    @Delete
    fun deleteArticleMedia(articleMedia: ArticleMedia)

    @Query("SELECT * FROM articleMedia WHERE articleID = :articleID")
    fun getMediaByArticleId(articleID: Int): Flow<ArticleMedia>

    @Query("SELECT * FROM articleMedia")
    fun allArticleMedia(): Flow<List<ArticleMedia>>

    @Query("SELECT * FROM articleMedia")
    suspend fun allArticleMediaN(): List<ArticleMedia>
}
