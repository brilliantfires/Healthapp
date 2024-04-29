package com.example.healthapp.data.repository

import com.example.healthapp.data.dao.AuthorsDAO
import com.example.healthapp.data.entity.Author
import com.example.healthapp.data.mysql.RetrofitClient.apiService
import kotlinx.coroutines.flow.Flow

class AuthorsRepository(private val authorsDAO: AuthorsDAO) {
    fun getAllAuthors(): Flow<List<Author>> = authorsDAO.getAllAuthors()

    suspend fun getAllAuthorsN(): List<Author> {
        return authorsDAO.getAllAuthorsN()
    }

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

    // 从网络获取作者并存储到数据库
    suspend fun getAllAuthorsAndStore() {
        val response = apiService.getAllAuthors()
        if (response.isSuccessful) {
            response.body()?.let { authors ->
                authorsDAO.insertAll(authors)
            }
        }
    }
}
