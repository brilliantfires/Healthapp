package com.example.healthapp.data.repository

import com.example.healthapp.data.dao.ArticlesDAO
import com.example.healthapp.data.entity.Article
import com.example.healthapp.data.mysql.RetrofitClient
import kotlinx.coroutines.flow.Flow

class ArticlesRepository(private val articlesDAO: ArticlesDAO) {
    fun getAllArticles(): Flow<List<Article>> = articlesDAO.getAllArticles()
    suspend fun getAllArticlesN(): List<Article> {
        return articlesDAO.getAllArticlesN()
    }

    suspend fun insertArticle(article: Article) {
        articlesDAO.insertArticle(article)
    }

    suspend fun updateArticle(article: Article) {
        articlesDAO.updateArticle(article)
    }

    suspend fun deleteArticle(article: Article) {
        articlesDAO.deleteArticle(article)
    }

    fun getArticleById(articleID: Int): Flow<Article> = articlesDAO.getArticleById(articleID)

    // 根据tagID来获取文章列表
    fun getArticleByTagId(tagId: Int): Flow<List<Article>> {
        return articlesDAO.getArticleByTagId(tagId)
    }

    // 从网络获取作者并存储到数据库
    suspend fun getAllArticlesAndStore() {
        val response = RetrofitClient.apiService.getAllArticles()
        if (response.isSuccessful) {
            response.body()?.let { authors ->
                articlesDAO.insertAll(authors)
            }
        }
    }
}
