package com.example.healthapp.data.repository

import com.example.healthapp.data.dao.ArticleTagRelationDAO
import com.example.healthapp.data.entity.Article
import com.example.healthapp.data.entity.ArticleTag
import com.example.healthapp.data.entity.ArticleTagRelation
import com.example.healthapp.data.mysql.RetrofitClient
import kotlinx.coroutines.flow.Flow

class ArticleTagRelationRepository(private val articleTagRelationDAO: ArticleTagRelationDAO) {
    fun getTagsForArticle(articleID: Int): Flow<List<ArticleTag>> =
        articleTagRelationDAO.getTagsForArticleTagRelation(articleID)

    fun getAllArticleTagRelations(): Flow<List<ArticleTagRelation>> =
        articleTagRelationDAO.getAllArticleTagRelations()

    suspend fun getAllArticleTagRelationsN(): List<ArticleTagRelation> =
        articleTagRelationDAO.getAllArticleTagRelationsN()

    suspend fun updateArticleTagRelation(articleTagRelation: ArticleTagRelation) {
        articleTagRelationDAO.updateArticleTagRelation(articleTagRelation)
    }

    suspend fun deleteArticleTagRelation(articleTagRelation: ArticleTagRelation) {
        articleTagRelationDAO.deleteArticleTagRelation(articleTagRelation)
    }

    fun getArticlesForTag(tagID: Int): Flow<List<Article>> =
        articleTagRelationDAO.getArticleTagRelationsForTag(tagID)

    suspend fun insertArticleTagRelation(articleTagRelation: ArticleTagRelation) {
        articleTagRelationDAO.insertArticleTagRelation(articleTagRelation)
    }

    // 获取选为展示的Tag(项目的标题)
    fun getDisplayTag(): Flow<List<ArticleTag>> {
        return articleTagRelationDAO.getDisplayTag()
    }

    fun getArticleTagRelationByTagId(tagId: Int): Flow<List<ArticleTagRelation>> {
        return articleTagRelationDAO.getArticleTagRelationByTagId(tagId)
    }

    suspend fun getTagRelationByTagId(tagId: Int): List<ArticleTagRelation> {
        return articleTagRelationDAO.getTagRelationByTagId(tagId)
    }

    // 从网络获取作者并存储到数据库
    suspend fun getAllTagRelationsAndStore() {
        val response = RetrofitClient.apiService.getAllArticleTagRelations()
        if (response.isSuccessful) {
            response.body()?.let { articleTagRelations ->
                articleTagRelationDAO.insertAll(articleTagRelations)
            }
        }
    }
}
