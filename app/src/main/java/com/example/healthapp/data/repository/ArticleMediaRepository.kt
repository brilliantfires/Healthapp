package com.example.healthapp.data.repository

import com.example.healthapp.data.dao.ArticleMediaDAO
import com.example.healthapp.data.entity.ArticleMedia
import com.example.healthapp.data.mysql.RetrofitClient
import kotlinx.coroutines.flow.Flow

class ArticleMediaRepository(private val articleMediaDAO: ArticleMediaDAO) {
    fun getMediaByArticleId(articleID: Int): Flow<ArticleMedia> =
        articleMediaDAO.getMediaByArticleId(articleID)

    suspend fun insertArticleMedia(articleMedia: ArticleMedia) {
        articleMediaDAO.insertArticleMedia(articleMedia)
    }

    suspend fun updateArticleMedia(articleMedia: ArticleMedia) {
        articleMediaDAO.updateArticleMedia(articleMedia)
    }

    suspend fun deleteArticleMedia(articleMedia: ArticleMedia) {
        articleMediaDAO.deleteArticleMedia(articleMedia)
    }

    fun allArticleMedia() = articleMediaDAO.allArticleMedia()
    suspend fun getAllArticleMediaN(): List<ArticleMedia> {
        return articleMediaDAO.allArticleMediaN()
    }

    // 从网络获取作者并存储到数据库
    suspend fun getAllArticleMediaAndStore() {
        val response = RetrofitClient.apiService.getAllArticleMedia()
        if (response.isSuccessful) {
            response.body()?.let { authors ->
                articleMediaDAO.insertAll(authors)
            }
        }
    }
}
