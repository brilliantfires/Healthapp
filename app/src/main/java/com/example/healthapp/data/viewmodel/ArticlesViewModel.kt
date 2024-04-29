package com.example.healthapp.data.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.healthapp.data.entity.Article
import com.example.healthapp.data.repository.ArticlesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ArticlesViewModel(private val repository: ArticlesRepository) : ViewModel() {

    val allArticles = repository.getAllArticles()

    fun insertArticle(article: Article) = viewModelScope.launch {
        repository.insertArticle(article)
    }

    fun updateArticle(article: Article) = viewModelScope.launch {
        repository.updateArticle(article)
    }

    fun deleteArticle(article: Article) = viewModelScope.launch {
        repository.deleteArticle(article)
    }

    fun getArticleById(articleID: Int) = repository.getArticleById(articleID)

    // 根据标签来获取文章列表
    fun getArticleByTagId(tagId: Int): Flow<List<Article>> {
        return repository.getArticleByTagId(tagId)
    }

    private val _articles = MutableStateFlow<List<Article>>(emptyList())
    val articles: StateFlow<List<Article>> = _articles.asStateFlow()

    fun loadArticlesOnce() {
        viewModelScope.launch(Dispatchers.IO) {
            val articles = repository.getAllArticlesN()
            _articles.value = articles
        }
    }

    fun getAllArticlesAndStore() {
        viewModelScope.launch {
            repository.getAllArticlesAndStore()
        }
    }
}

class ArticlesModelFactory(private val articlesRepository: ArticlesRepository) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ArticlesViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ArticlesViewModel(articlesRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}