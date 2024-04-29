package com.example.healthapp.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.healthapp.data.entity.Article
import kotlinx.coroutines.flow.Flow

@Dao
interface ArticlesDAO {
    @Insert
    fun insertArticle(article: Article)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(articles: List<Article>)

    @Update
    fun updateArticle(article: Article)

    @Delete
    fun deleteArticle(article: Article)

    @Query("SELECT * FROM articles WHERE articleID = :articleID")
    fun getArticleById(articleID: Int): Flow<Article>

    @Query("SELECT * FROM articles WHERE authorID = :authorID")
    fun getArticlesByAuthor(authorID: Int): Flow<List<Article>>

    @Query("SELECT * FROM articles ORDER BY publishDate DESC")
    fun getAllArticles(): Flow<List<Article>>

    @Query("SELECT * FROM articles")
    suspend fun getAllArticlesN(): List<Article>

    // 通过tagId来获取对应的文章列表
    @Query(
        "SELECT DISTINCT * " +
                "FROM articles " +
                "INNER JOIN articleTagRelation ON articles.articleID = articleTagRelation.articleID " +
                "WHERE articleTagRelation.tagID=:tagId"
    )
    fun getArticleByTagId(tagId: Int): Flow<List<Article>>
}
