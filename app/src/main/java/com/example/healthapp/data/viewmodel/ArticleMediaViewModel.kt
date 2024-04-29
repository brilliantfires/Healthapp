package com.example.healthapp.data.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.healthapp.data.entity.ArticleMedia
import com.example.healthapp.data.repository.ArticleMediaRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ArticleMediaViewModel(private val repository: ArticleMediaRepository) : ViewModel() {

    fun insertArticleMedia(articleMedia: ArticleMedia) = viewModelScope.launch {
        repository.insertArticleMedia(articleMedia)
    }

    fun updateArticleMedia(articleMedia: ArticleMedia) = viewModelScope.launch {
        repository.updateArticleMedia(articleMedia)
    }

    fun deleteArticleMedia(articleMedia: ArticleMedia) = viewModelScope.launch {
        repository.deleteArticleMedia(articleMedia)
    }

    fun getMediaByArticleId(articleID: Int) = repository.getMediaByArticleId(articleID)

    fun allArticleMedia(): Flow<List<ArticleMedia>> {
        return repository.allArticleMedia()
    }

    private val _articleMedia = MutableStateFlow<List<ArticleMedia>>(emptyList())
    val articleMedia: StateFlow<List<ArticleMedia>> = _articleMedia.asStateFlow()

    fun loadArticleMediaOnce() {
        viewModelScope.launch(Dispatchers.IO) {
            val media = repository.getAllArticleMediaN()
            _articleMedia.value = media
        }
    }

    fun getAllArticleMediaAndStore() {
        viewModelScope.launch {
            repository.getAllArticleMediaAndStore()
        }
    }
}

class ArticleMediaModelFactory(private val articleMediaRepository: ArticleMediaRepository) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ArticleMediaViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ArticleMediaViewModel(articleMediaRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}