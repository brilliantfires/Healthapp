package com.example.healthapp.data.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.healthapp.data.entity.ArticleTag
import com.example.healthapp.data.repository.ArticleTagsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class ArticleTagsViewModel(private val repository: ArticleTagsRepository) : ViewModel() {

    val allTags = repository.getAllTags()

    fun insertArticleTag(articleTag: ArticleTag) = viewModelScope.launch {
        repository.insertArticleTag(articleTag)
    }

    fun updateArticleTag(articleTag: ArticleTag) = viewModelScope.launch {
        repository.updateArticleTag(articleTag)
    }

    fun deleteArticleTag(articleTag: ArticleTag) = viewModelScope.launch {
        repository.deleteArticleTag(articleTag)
    }

    // 通过tagID来找Tag
    fun getTagById(tagId: Int): Flow<ArticleTag> {
        return repository.getTagById(tagId)
    }
}

class ArticleTagsModelFactory(private val articleTagsRepository: ArticleTagsRepository) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ArticleTagsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ArticleTagsViewModel(articleTagsRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
