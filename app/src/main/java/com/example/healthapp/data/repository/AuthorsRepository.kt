package com.example.healthapp.data.repository

import com.example.healthapp.data.dao.AuthorsDAO
import com.example.healthapp.data.entity.Author
import kotlinx.coroutines.flow.Flow

class AuthorsRepository(private val authorsDAO: AuthorsDAO) {
    fun getAllAuthors(): Flow<List<Author>> = authorsDAO.getAllAuthors()

    suspend fun insertAuthor(author: Author) {
        authorsDAO.insertAuthor(author)
    }

    suspend fun updateAuthor(author: Author) {
        authorsDAO.updateAuthor(author)
    }

    suspend fun deleteAuthor(author: Author) {
        authorsDAO.deleteAuthor(author)
    }

    fun getAuthorById(authorID: Int): Flow<Author> = authorsDAO.getAuthorById(authorID)
}
