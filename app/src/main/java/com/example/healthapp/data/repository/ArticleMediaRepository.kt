package com.example.healthapp.data.repository

import com.example.healthapp.data.dao.ArticleMediaDAO
import com.example.healthapp.data.entity.ArticleMedia
import kotlinx.coroutines.flow.Flow

class ArticleMediaRepository(private val articleMediaDAO: ArticleMediaDAO) {
    fun getMediaByArticleId(articleID: Int): Flow<ArticleMedia> = articleMediaDAO.getMediaByArticleId(articleID)

    suspend fun insertArticleMedia(articleMedia: ArticleMedia) {
        articleMediaDAO.insertArticleMedia(articleMedia)
    }

    suspend fun updateArticleMedia(articleMedia: ArticleMedia) {
        articleMediaDAO.updateArticleMedia(articleMedia)
    }

    suspend fun deleteArticleMedia(articleMedia: ArticleMedia) {
        articleMediaDAO.deleteArticleMedia(articleMedia)
    }
}
