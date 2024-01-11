package com.example.healthapp.data.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.healthapp.data.entity.User
import com.example.healthapp.data.entity.WellnessTask
import kotlinx.coroutines.flow.Flow

@Dao
interface WellnessTaskDAO {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertTask(wellnessTask: WellnessTask)

    @Update
    suspend fun updateTask(wellnessTask: WellnessTask)

    @Delete
    suspend fun deleteTask(wellnessTask: WellnessTask)

    @Query("SELECT * FROM WellnessTask WHERE taskId = :taskId")
    fun getTaskById(taskId: Int): Flow<WellnessTask>

    @Query("SELECT * FROM WellnessTask")
    fun getAllTasks(): Flow<List<WellnessTask>>
}
