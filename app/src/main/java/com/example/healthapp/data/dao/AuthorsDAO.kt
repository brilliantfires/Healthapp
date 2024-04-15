package com.example.healthapp.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.healthapp.data.entity.Author
import kotlinx.coroutines.flow.Flow

@Dao
interface AuthorsDAO {
    @Insert
    fun insertAuthor(author: Author)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(authors: List<Author>)

    @Update
    fun updateAuthor(author: Author)

    @Delete
    fun deleteAuthor(author: Author)

    @Query("SELECT * FROM authors WHERE authorID = :authorID")
    fun getAuthorById(authorID: Int): Flow<Author>

    @Query("SELECT * FROM authors")
    fun getAllAuthors(): Flow<List<Author>>
}
