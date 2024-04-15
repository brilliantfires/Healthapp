package com.example.healthapp.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.healthapp.data.entity.ExerciseRecord
import kotlinx.coroutines.flow.Flow

@Dao
interface ExerciseRecordDAO {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(exerciseRecord: ExerciseRecord)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(exerciseRecords: List<ExerciseRecord>)

    @Update
    suspend fun updateExerciseRecord(exerciseRecord: ExerciseRecord)

    @Delete
    suspend fun deleteExerciseRecord(exerciseRecord: ExerciseRecord)

    @Query("SELECT * FROM exerciseRecord WHERE exerciseID = :exerciseID")
    fun getExerciseRecordById(exerciseID: Int): Flow<ExerciseRecord>

    @Query("SELECT * FROM exerciseRecord")
    fun getAllExerciseRecords(): Flow<List<ExerciseRecord>>
}