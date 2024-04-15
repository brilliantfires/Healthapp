package com.example.healthapp.data.repository

import com.example.healthapp.data.dao.ArticleTagsDAO
import com.example.healthapp.data.entity.ArticleTag
import kotlinx.coroutines.flow.Flow

class ArticleTagsRepository(private val articleTagsDAO: ArticleTagsDAO) {
    fun getAllTags(): Flow<List<ArticleTag>> = articleTagsDAO.getAllTags()

    suspend fun insertArticleTag(articleTag: ArticleTag) {
        articleTagsDAO.insertArticleTag(articleTag)
    }

    suspend fun updateArticleTag(articleTag: ArticleTag) {
        articleTagsDAO.updateArticleTag(articleTag)
    }

    suspend fun deleteArticleTag(articleTag: ArticleTag) {
        articleTagsDAO.deleteArticleTag(articleTag)
    }

    // 通过tagID来找Tag
    fun getTagById(tagId: Int): Flow<ArticleTag> {
        return articleTagsDAO.getTagById(tagId)
    }
}
