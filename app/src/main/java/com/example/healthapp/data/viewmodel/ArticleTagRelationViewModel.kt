package com.example.healthapp.data.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.healthapp.data.entity.ArticleTagRelation
import com.example.healthapp.data.repository.ArticleTagRelationRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ArticleTagRelationViewModel(private val repository: ArticleTagRelationRepository) :
    ViewModel() {

    val allArticleTagRelations = repository.getAllArticleTagRelations()

    fun insertArticleTagRelation(articleTagRelation: ArticleTagRelation) = viewModelScope.launch {
        repository.insertArticleTagRelation(articleTagRelation)
    }

    fun updateArticleTagRelation(articleTagRelation: ArticleTagRelation) = viewModelScope.launch {
        repository.updateArticleTagRelation(articleTagRelation)
    }

    fun deleteArticleTagRelation(articleTagRelation: ArticleTagRelation) = viewModelScope.launch {
        repository.deleteArticleTagRelation(articleTagRelation)
    }

    //获取选择展示的Tag
    val getDisplayTag = repository.getDisplayTag()

    // 跟新符合要求的的tagId的所有的isDisplayed
    fun updateCategoryStatus(tagId: Int, isDisplayed: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            val tagRelations = repository.getTagRelationByTagId(tagId)
            tagRelations.forEach { tagRelation ->
                val update = tagRelation.copy(isDisplayed = isDisplayed)
                repository.updateArticleTagRelation(update)
            }
        }
    }


    fun getArticleTagRelationByTagId(tagId: Int): Flow<List<ArticleTagRelation>> {
        return repository.getArticleTagRelationByTagId(tagId)
    }

    private val _articleTagRelations = MutableStateFlow<List<ArticleTagRelation>>(emptyList())
    val articleTagRelations: StateFlow<List<ArticleTagRelation>> =
        _articleTagRelations.asStateFlow()

    fun loadArticleTagRelationsOnce() {
        viewModelScope.launch(Dispatchers.IO) {
            val tagRelations = repository.getAllArticleTagRelationsN()
            _articleTagRelations.value = tagRelations
        }
    }

    fun getAllArticleTagRelationsAndStore() {
        viewModelScope.launch {
            repository.getAllTagRelationsAndStore()
        }
    }
}

class ArticleTagRelationModelFactory(private val articleTagRelationRepository: ArticleTagRelationRepository) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ArticleTagRelationViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ArticleTagRelationViewModel(articleTagRelationRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}