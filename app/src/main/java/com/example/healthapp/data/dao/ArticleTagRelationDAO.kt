package com.example.healthapp.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.healthapp.data.entity.Article
import com.example.healthapp.data.entity.ArticleTag
import com.example.healthapp.data.entity.ArticleTagRelation
import kotlinx.coroutines.flow.Flow

@Dao
interface ArticleTagRelationDAO {
    @Insert
    fun insertArticleTagRelation(articleTagRelation: ArticleTagRelation)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(articleTagRelations: List<ArticleTagRelation>)

    @Update
    fun updateArticleTagRelation(articleTagRelation: ArticleTagRelation)

    @Delete
    fun deleteArticleTagRelation(articleTagRelation: ArticleTagRelation)

    @Query("SELECT * FROM articleTagRelation")
    fun getAllArticleTagRelations(): Flow<List<ArticleTagRelation>>

    @Query(
        "" +
                "SELECT * " +
                "FROM articleTags " +
                "INNER JOIN articleTagRelation ON articleTags.tagID = articleTagRelation.tagID " +
                "WHERE articleTagRelation.articleID = :articleID"
    )
    fun getTagsForArticleTagRelation(articleID: Int): Flow<List<ArticleTag>>

    @Query(
        "SELECT * " +
                "FROM articles " +
                "INNER JOIN articleTagRelation ON articles.articleID = articleTagRelation.articleID " +
                "WHERE articleTagRelation.tagID = :tagID"
    )
    fun getArticleTagRelationsForTag(tagID: Int): Flow<List<Article>>

    // 获取选择展示的Tag，也就是isDisplay为true的
    @Query(
        "SELECT DISTINCT * " +
                "FROM articleTags " +
                "INNER JOIN articleTagRelation ON articleTagRelation.tagID=articleTags.tagID " +
                "WHERE articleTagRelation.isDisplayed=true"
    )
    fun getDisplayTag(): Flow<List<ArticleTag>>

    @Query("SELECT * FROM articleTagRelation WHERE tagID=:tagId")
    fun getArticleTagRelationByTagId(tagId: Int): Flow<List<ArticleTagRelation>>

    @Query("SELECT * FROM articleTagRelation WHERE tagID=:tagId")
    suspend fun getTagRelationByTagId(tagId: Int): List<ArticleTagRelation>
}
