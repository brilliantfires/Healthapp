package com.example.healthapp.data.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.healthapp.data.entity.Author
import com.example.healthapp.data.repository.AuthorsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AuthorsViewModel(private val repository: AuthorsRepository) : ViewModel() {

    val allAuthors = repository.getAllAuthors()

    fun insertAuthor(author: Author) = viewModelScope.launch {
        repository.insertAuthor(author)
    }

    fun updateAuthor(author: Author) = viewModelScope.launch {
        repository.updateAuthor(author)
    }

    fun deleteAuthor(author: Author) = viewModelScope.launch {
        repository.deleteAuthor(author)
    }

    fun getAuthorById(authorID: Int) = repository.getAuthorById(authorID)

    private val _authors = MutableStateFlow<List<Author>>(emptyList())
    val authors: StateFlow<List<Author>> = _authors.asStateFlow()

    fun loadAuthorsOnce() {
        viewModelScope.launch(Dispatchers.IO) {
            val authors = repository.getAllAuthorsN()
            _authors.value = authors
        }
    }

    fun getAllAuthorsAndStore() {
        viewModelScope.launch {
            repository.getAllAuthorsAndStore()
        }
    }
}

class AuthorsModelFactory(private val authorsRepository: AuthorsRepository) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AuthorsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AuthorsViewModel(authorsRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}